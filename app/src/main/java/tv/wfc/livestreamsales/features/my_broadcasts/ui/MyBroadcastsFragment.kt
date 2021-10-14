package tv.wfc.livestreamsales.features.my_broadcasts.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentMyBroadcastsBinding
import tv.wfc.livestreamsales.features.my_broadcasts.di.MyBroadcastsComponent
import tv.wfc.livestreamsales.features.my_broadcasts.model.NextDestination
import tv.wfc.livestreamsales.features.my_broadcasts.view_model.IMyBroadcastsViewModel
import javax.inject.Inject

class MyBroadcastsFragment: BaseFragment(R.layout.fragment_my_broadcasts) {
    private val navigationController by lazy { findNavController() }

    private var viewBinding: FragmentMyBroadcastsBinding? = null

    private lateinit var dependenciesComponent: MyBroadcastsComponent

    @Inject
    lateinit var viewModel: IMyBroadcastsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeDependenciesComponent()
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

    private fun initializeDependenciesComponent() {
        if(::dependenciesComponent.isInitialized) return
        dependenciesComponent = appComponent.myBroadcastsComponent()
            .fragment(this)
            .build()
    }

    private fun injectDependencies() {
        dependenciesComponent.inject(this)
    }

    private fun bindView(view: View) {
        viewBinding = FragmentMyBroadcastsBinding.bind(view)
    }

    private fun unbindView() {
        viewBinding = null
    }

    private fun initializeContentLoader() {
        viewBinding?.contentLoaderView?.apply {
            clearPreparationListeners()
            addOnDataIsPreparedListener(::onDataIsPrepared)
            attachViewModel(viewLifecycleOwner, viewModel)
            viewModel.isAnyOperationInProgress.observe(viewLifecycleOwner) { isAnyOperationInProgress ->
                when(isAnyOperationInProgress) {
                    true -> showOperationProgress()
                    false -> hideOperationProgress()
                }
            }
        }
    }

    private fun onDataIsPrepared() {

    }

    private fun manageNavigation() {
        viewModel.nextDestinationEvent.observe(viewLifecycleOwner) { nextDestination ->
            when(nextDestination) {
                NextDestination.Close -> navigationController.navigateUp()
            }
        }
    }
}