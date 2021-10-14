package tv.wfc.livestreamsales.features.broadcast_creation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentBroadcastCreationBinding
import tv.wfc.livestreamsales.features.broadcast_creation.di.BroadcastCreationComponent
import tv.wfc.livestreamsales.features.broadcast_creation.model.NextDestination
import tv.wfc.livestreamsales.features.broadcast_creation.view_model.IBroadcastCreationViewModel
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import javax.inject.Inject

class BroadcastCreationFragment: BaseFragment(R.layout.fragment_broadcast_creation) {
    private val navigationController by lazy { findNavController() }

    private val onToolbarBackPressed = object: MainAppContentActivity.ToolbarNavigationOnClickListener{
        override fun onClick() {
            (requireActivity() as MainAppContentActivity).removeToolbarNavigationOnClickListener(this)
            viewModel.intentToCloseCurrentDestination()
        }
    }

    private lateinit var dependenciesComponent: BroadcastCreationComponent

    private var viewBinding: FragmentBroadcastCreationBinding? = null

    @Inject
    lateinit var viewModel: IBroadcastCreationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeDependenciesComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindActivityToolbar()
        bindView(view)
        initializeContentLoader()
        manageNavigation()
    }

    override fun onDestroyView() {
        unbindActivityToolbar()
        unbindView()
        super.onDestroyView()
    }

    private fun initializeDependenciesComponent() {
        if(::dependenciesComponent.isInitialized) return

        dependenciesComponent = appComponent
            .broadcastCreationComponentFactory()
            .create(this)
    }

    private fun injectDependencies() {
        dependenciesComponent.inject(this)
    }

    private fun bindView(view: View) {
        viewBinding = FragmentBroadcastCreationBinding.bind(view)
    }

    private fun unbindView() {
        viewBinding = null
    }

    private fun initializeContentLoader() {
        viewBinding?.contentLoader?.run {
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

    private fun manageNavigation() {
        viewModel.nextDestinationEvent.observe(viewLifecycleOwner) { nextDestination ->
            when(nextDestination) {
                NextDestination.Close -> navigationController.navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this){
            viewModel.intentToCloseCurrentDestination()
        }
    }

    private fun onDataIsPrepared() {

    }

    private fun bindActivityToolbar() {
        (requireActivity() as MainAppContentActivity).addToolbarNavigationOnClickListener(onToolbarBackPressed)
    }

    private fun unbindActivityToolbar() {
        (requireActivity() as MainAppContentActivity).removeToolbarNavigationOnClickListener(onToolbarBackPressed)
    }
}