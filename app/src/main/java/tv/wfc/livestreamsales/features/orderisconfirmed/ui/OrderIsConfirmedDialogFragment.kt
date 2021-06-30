package tv.wfc.livestreamsales.features.orderisconfirmed.ui

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
import tv.wfc.livestreamsales.databinding.DialogOrderIsConfirmedBinding
import tv.wfc.livestreamsales.features.needpaymentinformation.ui.NeedPaymentInformationDialogFragmentDirections
import tv.wfc.livestreamsales.features.orderisconfirmed.di.OrderIsConfirmedComponent
import tv.wfc.livestreamsales.features.orderisconfirmed.viewmodel.IOrderIsConfirmedViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OrderIsConfirmedDialogFragment: BaseDialogFragment(R.layout.dialog_order_is_confirmed) {
    private val navigationController by lazy{ findNavController() }

    private var viewBinding: DialogOrderIsConfirmedBinding? = null

    private lateinit var orderIsConfirmedComponent: OrderIsConfirmedComponent

    @Inject
    lateinit var viewModel: IOrderIsConfirmedViewModel

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
        createOrderIsConfirmedComponent()
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

    private fun createOrderIsConfirmedComponent(){
        if(::orderIsConfirmedComponent.isInitialized) return

        orderIsConfirmedComponent = appComponent
            .orderIsConfirmedComponent()
            .create(this)
    }

    private fun injectDependencies(){
        orderIsConfirmedComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = DialogOrderIsConfirmedBinding.bind(view)
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
                .subscribeBy (
                    onNext = { exit() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeConfirmButton(){
        viewBinding?.okButton?.run{
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

    private fun exit(){
        navigationController.navigateUp()
    }
}