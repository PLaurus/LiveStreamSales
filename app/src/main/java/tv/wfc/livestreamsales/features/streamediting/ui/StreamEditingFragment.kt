package tv.wfc.livestreamsales.features.streamediting.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentBroadcastEditingBinding
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import tv.wfc.livestreamsales.features.streamediting.di.StreamEditingComponent
import tv.wfc.livestreamsales.features.streamediting.model.NextDestination
import tv.wfc.livestreamsales.features.streamediting.viewmodel.IStreamEditingViewModel
import javax.inject.Inject

class StreamEditingFragment: BaseFragment(R.layout.fragment_broadcast_editing) {
    private val navigationController by lazy { findNavController() }
    private val navigationArguments by navArgs<StreamEditingFragmentArgs>()

    private val onToolbarBackPressed = MainAppContentActivity.ToolbarNavigationOnClickListener {
        viewModel.requestToCloseCurrentDestination()
    }

    private lateinit var dependenciesComponent: StreamEditingComponent

    private var viewBinding: FragmentBroadcastEditingBinding? = null

    @Inject
    lateinit var viewModel: IStreamEditingViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeDependenciesComponent()
        injectDependencies()
        prepareViewModel()
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
            .streamEditingComponentFactory()
            .create(this)
    }

    private fun injectDependencies() {
        dependenciesComponent.inject(this)
    }

    private fun prepareViewModel() {
        val streamId = navigationArguments.streamId
        viewModel.prepare(streamId)
    }

    private fun bindView(view: View) {
        viewBinding = FragmentBroadcastEditingBinding.bind(view)
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

    private fun onDataIsPrepared() {

    }

    private fun manageNavigation() {
        viewModel.nextDestinationEvent.observe(viewLifecycleOwner) { nextDestination ->
            when(nextDestination) {
                NextDestination.Close -> navigationController.navigateUp()
            }
        }

        (requireActivity() as MainAppContentActivity).onBackPressedDispatcher.addCallback {
            viewModel.requestToCloseCurrentDestination()
        }
    }

    private fun bindActivityToolbar() {
        (requireActivity() as MainAppContentActivity).addToolbarNavigationOnClickListener(onToolbarBackPressed)
    }

    private fun unbindActivityToolbar() {
        (requireActivity() as MainAppContentActivity).removeToolbarNavigationOnClickListener(onToolbarBackPressed)
    }
}