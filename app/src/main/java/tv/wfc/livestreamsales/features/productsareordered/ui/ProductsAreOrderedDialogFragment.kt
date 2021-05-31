package tv.wfc.livestreamsales.features.productsareordered.ui

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
import tv.wfc.livestreamsales.databinding.DialogProductsAreOrderedBinding
import tv.wfc.livestreamsales.features.productsareordered.di.ProductsAreOrderedComponent
import tv.wfc.livestreamsales.features.productsareordered.viewmodel.IProductsAreOrderedViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductsAreOrderedDialogFragment: BaseDialogFragment(R.layout.dialog_products_are_ordered) {
    private val navigationController by lazy{ findNavController() }

    private var viewBinding: DialogProductsAreOrderedBinding? = null

    private lateinit var productsAreOrderedComponent: ProductsAreOrderedComponent

    @Inject
    lateinit var viewModel: IProductsAreOrderedViewModel

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
        createProductsAreOrderedComponent()
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

    private fun createProductsAreOrderedComponent(){
        if(::productsAreOrderedComponent.isInitialized) return

        productsAreOrderedComponent = appComponent
            .productsAreOrderedComponent()
            .create(this)
    }

    private fun injectDependencies(){
        productsAreOrderedComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = DialogProductsAreOrderedBinding.bind(view)
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
                .subscribeBy(
                    onNext = { exit() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeConfirmButton(){
        viewBinding?.confirmButton?.run{
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