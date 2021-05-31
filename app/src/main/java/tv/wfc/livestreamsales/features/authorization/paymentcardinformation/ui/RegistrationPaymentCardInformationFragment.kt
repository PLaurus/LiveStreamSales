package tv.wfc.livestreamsales.features.authorization.paymentcardinformation.ui

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import ru.yoomoney.sdk.kassa.payments.Checkout
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentRegistrationPaymentCardInformationBinding
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.RegistrationPaymentCardInformationComponent
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.viewmodel.IRegistrationPaymentCardInformationViewModel
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RegistrationPaymentCardInformationFragment: BaseFragment(R.layout.fragment_registration_payment_card_information) {
    private companion object{
        private const val REQUEST_CODE_TOKENIZE = 0
    }

    private val navigationController by lazy { findNavController() }

    private var viewBinding: FragmentRegistrationPaymentCardInformationBinding? = null

    private lateinit var registrationPaymentCardInformationComponent: RegistrationPaymentCardInformationComponent

    @Inject
    lateinit var viewModel: IRegistrationPaymentCardInformationViewModel

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createRegistrationPaymentCardInformationComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_TOKENIZE){
            if(data == null) return
            val viewBinding = this.viewBinding ?: return

            when(resultCode){
                RESULT_OK -> {
                    val tokenizationResult = Checkout.createTokenizationResult(data)
                    val token = tokenizationResult.paymentToken

                    viewModel.updatePaymentCardInformation(tokenizationResult.paymentToken)

                    AlertDialog.Builder(viewBinding.root.context)
                        .setTitle("Токен")
                        .setMessage(token)
                        .setPositiveButton("Ясно, понятно") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }
                RESULT_CANCELED -> {
                    Snackbar.make(viewBinding.root, "Не удалось провести токенизацию", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    private fun createRegistrationPaymentCardInformationComponent(){
        val authorizationComponent = appComponent
            .authorizationComponent()
            .create()

        registrationPaymentCardInformationComponent = authorizationComponent
            .registrationPaymentCardInformationComponent()
            .create(this)
    }

    private fun injectDependencies(){
        registrationPaymentCardInformationComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentRegistrationPaymentCardInformationBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeContentLoader(){
        viewBinding?.contentLoader?.apply {
            clearPreparationListeners()
            attachViewModel(viewLifecycleOwner, viewModel)
            addOnDataIsPreparedListener(::onDataIsPrepared)

            viewModel.isAnyOperationInProgress.observe(viewLifecycleOwner){ isAnyOperationInProgress ->
                if(isAnyOperationInProgress){
                    showOperationProgress()
                } else {
                    hideOperationProgress()
                }
            }
        }
    }

    private fun onDataIsPrepared() {
        initializeLinkPaymentCardButton()
        initializeSkipButton()
    }

    private fun initializeLinkPaymentCardButton(){
        viewBinding?.linkPaymentCardButton?.run{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { navigateToPaymentCardLinker() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeSkipButton(){
        viewBinding?.skipButton?.run{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { exit() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun navigateToPaymentCardLinker(){
        val context = this.context ?: return

        val paymentParameters = PaymentParameters(
            amount = Amount(BigDecimal.ONE, Currency.getInstance("RUB")),
            title = "Привязка карты",
            subtitle = "Списание 1 руб. для привязки карты",
            clientApplicationKey = "test_Nzk3NTQ1kYF1Pqhqr2tcjS7kBRC3hIaCffHRNPKktIg",
            shopId = "797545",
            savePaymentMethod = SavePaymentMethod.ON,
            paymentMethodTypes = setOf(PaymentMethodType.BANK_CARD)
        )


        val tokenizeIntent = Checkout.createTokenizeIntent(
            context,
            paymentParameters,
        )

        startActivityForResult(tokenizeIntent, REQUEST_CODE_TOKENIZE)
    }

    private fun exit(){
        val action = RegistrationPaymentCardInformationFragmentDirections.actionExit()

        navigationController.navigate(action)
    }
}