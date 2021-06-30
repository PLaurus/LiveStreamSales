package tv.wfc.livestreamsales.features.paymentcardinformation.viewmodel

import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.tools.livedata.LiveEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.yoomoney.sdk.kassa.payments.Checkout
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.exception.threedsecure.YooKassa3DSecureException
import tv.wfc.livestreamsales.application.model.paymentcardinformation.PaymentCardInformation
import tv.wfc.livestreamsales.application.model.paymentcardinformation.ResultOfStartingPaymentCardBinding
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.IPaymentCardInformationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PaymentCardInformationViewModel @Inject constructor(
    private val context: Context,
    private val paymentCardInformationRepository: IPaymentCardInformationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IPaymentCardInformationViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)

    private var dataPreparationDisposable: Disposable? = null
    private var waitUntilCardIsBoundDisposable: Disposable? = null

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)

    override val isAnyOperationInProgress = MutableLiveData<Boolean>().apply {
        activeOperationsCount
            .observeOn(mainThreadScheduler)
            .map { it > 0 }
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val paymentCardBindingParameters: LiveData<PaymentParameters> = MutableLiveData(createPaymentCardBindingParameters())
    override val paymentCardBindingError = LiveEvent<String?>()
    override val paymentCardBindingConfirmationUrl = LiveEvent<String?>()
    override val isPaymentCardBound = MediatorLiveData<Boolean>().apply{
        addSource(paymentCardBindingState){ state ->
            when(state){
                IPaymentCardInformationViewModel.CardBindingState.Bound -> true
                else -> false
            }
        }
    }
    override val paymentCardBindingState = MutableLiveData<IPaymentCardInformationViewModel.CardBindingState>()
    override val boundPaymentCardNumber = MutableLiveData<String?>()

    init{
        prepareData()
    }

    override fun startPaymentCardBinding(tokenizationResultIntent: Intent) {
        val tokenizationResult = Checkout.createTokenizationResult(tokenizationResultIntent)
        val token = tokenizationResult.paymentToken

        paymentCardInformationRepository
            .startPaymentCardBinding(token)
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(
                onSuccess = ::processResultOfPaymentCardBinding,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun waitUntilCardIsBound() {
        waitUntilCardIsBoundDisposable?.dispose()

        waitUntilCardIsBoundDisposable = Single
            .create<PaymentCardInformation>{ emitter ->
                val disposables = CompositeDisposable()
                val maxAttemptsCount = 3
                val secondsToWaitBeforeNextAttempt = 5L
                var currentAttemptsCount = 0
                var isFirstAttempt = true

                emitter.setDisposable(disposables)

                fun checkIsPaymentCardBound(){
                    paymentCardInformationRepository
                        .getPaymentCardInformation()
                        .run {
                            if(isFirstAttempt){
                                isFirstAttempt = false
                                this
                            } else{
                                delaySubscription(secondsToWaitBeforeNextAttempt, TimeUnit.SECONDS)
                            }
                        }
                        .observeOn(mainThreadScheduler)
                        .doOnSubscribe{ ++currentAttemptsCount }
                        .subscribeBy(
                            onSuccess = { paymentCardInformation ->
                                when(paymentCardInformation.isBoundToAccount){
                                    true -> emitter.onSuccess(paymentCardInformation)
                                    false -> {
                                        if(currentAttemptsCount < maxAttemptsCount){
                                            checkIsPaymentCardBound()
                                        } else{
                                            val errorMessage = context.getString(R.string.fragment_payment_card_information_binding_default_error)

                                            try{
                                                throw Exception(errorMessage)
                                            } catch(ex: Exception) {
                                                emitter.onError(ex)
                                            }
                                        }
                                    }
                                }
                            },
                            onError = applicationErrorsLogger::logError
                        )
                        .addTo(disposables)
                }

                checkIsPaymentCardBound()
            }
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(
                onSuccess = { paymentCardInformation ->
                    this.paymentCardBindingState.value = IPaymentCardInformationViewModel.CardBindingState.Bound
                    this.boundPaymentCardNumber.value = context.getString(R.string.fragment_payment_card_information_number_text, paymentCardInformation.cardNumber)
                },
                onError = {
                    this.boundPaymentCardNumber.value = context.getString(R.string.fragment_payment_card_information_card_will_be_bound_soon)
                    this.paymentCardBindingState.value = IPaymentCardInformationViewModel.CardBindingState.WillBeBoundSoon
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    override fun notify3dsErrorOccurred(`3dsResultIntent`: Intent) {
        val extraErrorCode = `3dsResultIntent`
            .getIntExtra(Checkout.EXTRA_ERROR_CODE, 0)
            .let { if(it == -1) null else it }
            ?: return
        val errorDescription: String? = `3dsResultIntent`.getStringExtra(Checkout.EXTRA_ERROR_DESCRIPTION)
        val errorFailingUrl: String? = `3dsResultIntent`.getStringExtra(Checkout.EXTRA_ERROR_FAILING_URL)

        val errorName = when(extraErrorCode){
            WebViewClient.ERROR_UNKNOWN -> { // -1
                context.getString(R.string.fragment_payment_card_information_binding_3ds_unknown_error)
            }
            WebViewClient.ERROR_HOST_LOOKUP -> { // -2
                context.getString(R.string.fragment_payment_card_information_binding_3ds_host_lookup_error)
            }
            WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME -> { // -3
                context.getString(R.string.fragment_payment_card_information_binding_3ds_unsupported_auth_scheme_error)
            }
            WebViewClient.ERROR_AUTHENTICATION -> { // -4
                context.getString(R.string.fragment_payment_card_information_binding_3ds_authentication_error)
            }
            WebViewClient.ERROR_PROXY_AUTHENTICATION -> { // -5
                context.getString(R.string.fragment_payment_card_information_binding_3ds_proxy_authentication_error)
            }
            WebViewClient.ERROR_CONNECT -> { // -6
                context.getString(R.string.fragment_payment_card_information_binding_3ds_connect_error)
            }
            WebViewClient.ERROR_IO -> { // -7
                context.getString(R.string.fragment_payment_card_information_binding_3ds_io_error)
            }
            WebViewClient.ERROR_TIMEOUT -> { // -8
                context.getString(R.string.fragment_payment_card_information_binding_3ds_timeout_error)
            }
            WebViewClient.ERROR_REDIRECT_LOOP -> { // -9
                context.getString(R.string.fragment_payment_card_information_binding_3ds_redirect_loop_error)
            }
            WebViewClient.ERROR_UNSUPPORTED_SCHEME -> { // -10
                context.getString(R.string.fragment_payment_card_information_binding_3ds_unsupported_scheme_error)
            }
            WebViewClient.ERROR_FAILED_SSL_HANDSHAKE -> { // -11
                context.getString(R.string.fragment_payment_card_information_binding_3ds_failed_ssl_handshake_error)
            }
            WebViewClient.ERROR_BAD_URL -> { // -12
                context.getString(R.string.fragment_payment_card_information_binding_3ds_bad_url_error)
            }
            WebViewClient.ERROR_FILE -> { // -13
                context.getString(R.string.fragment_payment_card_information_binding_3ds_file_error)
            }
            WebViewClient.ERROR_FILE_NOT_FOUND -> { // -14
                context.getString(R.string.fragment_payment_card_information_binding_3ds_file_not_found_error)
            }
            WebViewClient.ERROR_TOO_MANY_REQUESTS -> { // -15
                context.getString(R.string.fragment_payment_card_information_binding_3ds_too_many_requests_error)
            }
            WebViewClient.ERROR_UNSAFE_RESOURCE -> { // -16
                context.getString(R.string.fragment_payment_card_information_binding_3ds_unsafe_resource_error)
            }
            Checkout.ERROR_NOT_HTTPS_URL -> { // -2147483648
                context.getString(R.string.fragment_payment_card_information_binding_3ds_not_http_url_error)
            }
            else -> null
        } ?: return

        try{
            throw YooKassa3DSecureException(
                errorName,
                errorDescription,
                errorFailingUrl
            )
        } catch (exception: YooKassa3DSecureException){
            applicationErrorsLogger.logError(exception)
            paymentCardBindingError.value = exception.errorName
        }
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    @Synchronized
    private fun prepareData(){
        dataPreparationDisposable?.dispose()

        dataPreparationDisposable = Completable
            .mergeArray(
                preparePaymentCardInformation()
            )
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .subscribeBy(
                onComplete = {
                    dataPreparationState.value = ViewModelPreparationState.DataIsPrepared
                },
                onError = {
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData()
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    private fun preparePaymentCardInformation(): Completable{
        return paymentCardInformationRepository
            .getPaymentCardInformation()
            .observeOn(mainThreadScheduler)
            .flatMapCompletable { paymentCardInformation ->
                Completable.fromRunnable {
                    this.paymentCardBindingState.value = when(paymentCardInformation.isBoundToAccount){
                        true -> IPaymentCardInformationViewModel.CardBindingState.Bound
                        else -> IPaymentCardInformationViewModel.CardBindingState.NotBound
                    }
                    this.boundPaymentCardNumber.value = context.getString(R.string.fragment_payment_card_information_number_text, paymentCardInformation.cardNumber)
                }
            }
    }

    private fun createPaymentCardBindingParameters(): PaymentParameters {
        val oneRuble = Amount(BigDecimal.ONE, Currency.getInstance("RUB"))
        val cardBindingTitle = context.getString(R.string.fragment_payment_card_information_yookassa_card_binding_title)
        val cardBindingSubtitle = context.getString(R.string.fragment_payment_card_information_yookassa_card_binding_subtitle)
        val yooKassaClientApplicationKey = BuildConfig.YOO_KASSA_CLIENT_APPLICATION_KEY
        val yooKassaShopId = BuildConfig.YOO_KASSA_SHOP_ID

        return PaymentParameters(
            amount = oneRuble,
            title = cardBindingTitle,
            subtitle = cardBindingSubtitle,
            clientApplicationKey = yooKassaClientApplicationKey,
            shopId = yooKassaShopId,
            savePaymentMethod = SavePaymentMethod.ON,
            paymentMethodTypes = setOf(PaymentMethodType.BANK_CARD)
        )
    }

    private fun processResultOfPaymentCardBinding(resultOfStartingPaymentCardBinding: ResultOfStartingPaymentCardBinding){
        val isBindingFlowStarted = resultOfStartingPaymentCardBinding.isBindingFlowStarted
        val defaultErrorMessage = context.getString(R.string.fragment_payment_card_information_binding_default_error)
        var errorMessage: String? = null //?: context.getString(R.string.fragment_registration_payment_card_information_binding_default_error)
        val confirmationUrl = resultOfStartingPaymentCardBinding.confirmationUrl

        if(!isBindingFlowStarted){
            errorMessage = resultOfStartingPaymentCardBinding.errorMessage ?: defaultErrorMessage
        } else if(confirmationUrl == null){
            errorMessage = defaultErrorMessage
        } else{
            paymentCardBindingConfirmationUrl.value = confirmationUrl
        }

        errorMessage?.let { paymentCardBindingError.value = it }
    }

    @Synchronized
    private fun incrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount + 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }

    @Synchronized
    private fun decrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount - 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }
}