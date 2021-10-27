package tv.wfc.livestreamsales.features.streamcreation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentStreamCreationBinding
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import tv.wfc.livestreamsales.features.streamcreation.di.StreamCreationComponent
import tv.wfc.livestreamsales.features.streamcreation.model.NextDestination
import tv.wfc.livestreamsales.features.streamcreation.viewmodel.IStreamCreationViewModel
import javax.inject.Inject

class StreamCreationFragment: BaseFragment(R.layout.fragment_stream_creation) {
    private val navigationController by lazy { findNavController() }

    private val onToolbarBackPressed = object: MainAppContentActivity.ToolbarNavigationOnClickListener{
        override fun onClick() {
            (requireActivity() as MainAppContentActivity).removeToolbarNavigationOnClickListener(this)
            viewModel.intentToCloseCurrentDestination()
        }
    }

    private lateinit var dependenciesComponent: StreamCreationComponent

    private var viewBinding: FragmentStreamCreationBinding? = null

    @Inject
    lateinit var viewModel: IStreamCreationViewModel

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
        viewBinding = FragmentStreamCreationBinding.bind(view)
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