package tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.edittextformatters.PhoneNumberTextFormatter
import com.laurus.p.tools.livedata.LiveEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.authorization.repository.ILoginRepository
import javax.inject.Inject

class PhoneNumberConfirmationViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    private val authorizationRepository: IAuthorizationRepository,
    private val loginRepository: ILoginRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IPhoneNumberConfirmationViewModel {
    companion object{
        private const val TERMS_OF_THE_OFFER_URL = "http://www.google.com/"
    }

    private val disposables = CompositeDisposable()

    private val dataPreparationStateSubject = PublishSubject.create<ViewModelPreparationState>()

    private val codeSubject = BehaviorSubject.createDefault("")
    private val isCodeBeingCheckedSubject = PublishSubject.create<Boolean>()
    private val phoneConfirmationResultSubject = PublishSubject.create<PhoneNumberConfirmationResult>()
    private val phoneConfirmationErrorsSubject = PublishSubject.create<String>()

    override val dataPreparationState: LiveData<ViewModelPreparationState> =
        MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared).apply {
            dataPreparationStateSubject
                .distinctUntilChanged()
                .observeOn(mainThreadScheduler)
                .subscribe(::setValue)
                .addTo(disposables)
        }

    override val authorizationToken = MutableLiveData<String?>()

    override val phoneNumber: LiveData<String> = MutableLiveData("").apply {
        loginRepository
            .getLogin()
            .map(::formatPhoneNumber)
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    private fun formatPhoneNumber(phoneNumberWithCountryCode: String): String{
        val phoneNumberWithoutCountryCode = phoneNumberWithCountryCode.removePrefix("+7")
        val formattedPhoneNumber = PhoneNumberTextFormatter().format(phoneNumberWithoutCountryCode)
        return "+7 $formattedPhoneNumber"
    }

    override val code: LiveData<String> = MutableLiveData("").apply {
        codeSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val codeLength: LiveData<Int> = MutableLiveData(0).apply{
        authorizationRepository.getRequiredCodeLength()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe {
                dataPreparationStateSubject.onNext(ViewModelPreparationState.DataIsBeingPrepared)
            }
            .doAfterSuccess {
                dataPreparationStateSubject.onNext(ViewModelPreparationState.DataIsPrepared)
            }
            .doOnError {
                dataPreparationStateSubject.onNext(ViewModelPreparationState.FailedToPrepareData())
            }
            .subscribeBy(
                onSuccess = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isCodeBeingChecked: LiveData<Boolean> = MutableLiveData(false).apply{
        isCodeBeingCheckedSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val phoneNumberConfirmationResult: LiveData<PhoneNumberConfirmationResult> =
        LiveEvent<PhoneNumberConfirmationResult>().apply {
            phoneConfirmationResultSubject
                .observeOn(mainThreadScheduler)
                .subscribe(::setValue)
                .addTo(disposables)
        }

    override val phoneNumberConfirmationErrors: LiveData<String> = LiveEvent<String>().apply{
        phoneConfirmationErrorsSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val newCodeRequestWaitingTime: LiveData<Long> = MutableLiveData(0L).apply{
        authorizationManager.nextCodeRequestWaitingTime
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val isCodeRequestAvailable: LiveData<Boolean> = MutableLiveData(false).apply{
        authorizationManager.isCodeRequestAvailable
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val termsOfTheOfferUrl: LiveData<String> = MutableLiveData(TERMS_OF_THE_OFFER_URL)

    override fun updateCode(code: String){
        codeSubject.onNext(code)
    }

    override fun confirmPhoneNumber() {
        val codeSingle = codeSubject
            .firstOrError()
            .map{ it.toInt() }

        val phoneNumberSingle = loginRepository
            .getLogin()

        Single
            .zip(codeSingle, phoneNumberSingle){ code, phoneNumber ->
                authorizationManager.confirmPhoneNumber(phoneNumber, code)
            }
            .flatMap { it }
            .flatMap { phoneNumberConfirmationResult ->
                val logInCompletable = when(phoneNumberConfirmationResult){
                    is PhoneNumberConfirmationResult.Confirmed -> {
                        authorizationManager
                            .logIn(phoneNumberConfirmationResult.token)
                            .subscribeOn(ioScheduler)
                    }
                    is PhoneNumberConfirmationResult.ConfirmedButNeedUserPersonalInformation -> {
                        authorizationManager
                            .logInTemporary(phoneNumberConfirmationResult.token)
                            .subscribeOn(ioScheduler)
                            .doOnComplete { authorizationToken.value = phoneNumberConfirmationResult.token }
                            .subscribeOn(mainThreadScheduler)
                    }
                    else -> Completable.complete()
                }

                logInCompletable.toSingleDefault(phoneNumberConfirmationResult)
            }
            .doOnSubscribe { isCodeBeingCheckedSubject.onNext(true) }
            .doOnTerminate { isCodeBeingCheckedSubject.onNext(false) }
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = { confirmationResult ->
                    phoneConfirmationResultSubject.onNext(confirmationResult)

                    if(confirmationResult is PhoneNumberConfirmationResult.NotConfirmed){
                        confirmationResult.errorMessage?.let{
                            phoneConfirmationErrorsSubject.onNext(it)
                        }
                    }
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun requestNewCode() {
        loginRepository
            .getLogin()
            .flatMap { phoneNumber ->
                authorizationManager.requestConfirmationCode(phoneNumber)
            }
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}