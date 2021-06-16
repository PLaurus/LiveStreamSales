package tv.wfc.livestreamsales.features.needpaymentinformation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseDialogFragment
import tv.wfc.livestreamsales.databinding.DialogNeedPaymentInformationBinding
import tv.wfc.livestreamsales.databinding.DialogProductsAreOrderedBinding
import tv.wfc.livestreamsales.features.needpaymentinformation.di.NeedPaymentInformationComponent
import tv.wfc.livestreamsales.features.needpaymentinformation.viewmodel.INeedPaymentInformationViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NeedPaymentInformationDialogFragment: BaseDialogFragment(R.layout.dialog_need_payment_information) {
    private val navigationController by lazy{ findNavController() }

    private var viewBinding: DialogNeedPaymentInformationBinding? = null

    private lateinit var needPaymentInformationComponent: NeedPaymentInformationComponent

    @Inject
    lateinit var viewModel: INeedPaymentInformationViewModel

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
        createNeedPaymentInformationComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
        manageNavigation()
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    private fun createNeedPaymentInformationComponent(){
        if(::needPaymentInformationComponent.isInitialized) return

        needPaymentInformationComponent = appComponent
            .needPaymentInformationComponent()
            .create(this)
    }

    private fun injectDependencies(){
        needPaymentInformationComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = DialogNeedPaymentInformationBinding.bind(view)
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

    private fun manageNavigation(){
        initializeCloseButton()
        initializeConfirmButton()
    }

    private fun onDataIsPrepared() = Unit

    private fun initializeCloseButton(){
        viewBinding?.closeButton?.run{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .doOnError(applicationErrorsLogger::logError)
                .subscribeBy(onNext = { exit() })
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeConfirmButton(){
        viewBinding?.confirmButton?.run{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .doOnError(applicationErrorsLogger::logError)
                .subscribeBy(onNext = { navigateToPaymentCardInformationDestination() })
                .addTo(viewScopeDisposables)
        }
    }

    private fun exit(){
        navigationController.navigateUp()
    }

    private fun navigateToPaymentCardInformationDestination(){
        val action = NeedPaymentInformationDialogFragmentDirections.actionToPaymentCardInformationDestination()
        navigationController.navigate(action)
    }
}