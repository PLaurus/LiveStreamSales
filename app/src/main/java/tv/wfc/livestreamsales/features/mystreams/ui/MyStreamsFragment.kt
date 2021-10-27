package tv.wfc.livestreamsales.features.mystreams.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentMyStreamsBinding
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import tv.wfc.livestreamsales.features.mystreams.di.MyStreamsComponent
import tv.wfc.livestreamsales.features.mystreams.model.NextDestination
import tv.wfc.livestreamsales.features.mystreams.ui.adapter.MyStreamsAdapter
import tv.wfc.livestreamsales.features.mystreams.viewmodel.IMyStreamsViewModel
import javax.inject.Inject

class MyStreamsFragment: BaseFragment(R.layout.fragment_my_streams) {
    private val navigationController by lazy { findNavController() }

    private val onToolbarBackPressed = object: MainAppContentActivity.ToolbarNavigationOnClickListener{
        override fun onClick() {
            (requireActivity() as MainAppContentActivity).removeToolbarNavigationOnClickListener(this)
            viewModel.intentToCloseCurrentDestination()
        }
    }

    private var viewBinding: FragmentMyStreamsBinding? = null

    private lateinit var dependenciesComponent: MyStreamsComponent

    @Inject
    lateinit var viewModel: IMyStreamsViewModel

    @Inject
    lateinit var myStreamsDiffUtilItemCallback: DiffUtil.ItemCallback<MyStream>

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeDependenciesComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindActivityToolbar()
        bindView(view)
        manageNavigation()
    }

    override fun onDestroyView() {
        unbindActivityToolbar()
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
        viewBinding = FragmentMyStreamsBinding.bind(view)
        viewBinding?.initialize()
    }

    private fun unbindView() {
        viewBinding = null
    }

    private fun FragmentMyStreamsBinding.initializeContentLoader() {
        contentLoaderView.apply {
            clearPreparationListeners()
            attachViewModel(viewLifecycleOwner, viewModel)

            viewModel.isAnyOperationInProgress.observe(viewLifecycleOwner) { isAnyOperationInProgress ->
                when(isAnyOperationInProgress) {
                    true -> showOperationProgress()
                    false -> hideOperationProgress()
                }
            }
        }
    }

    private fun FragmentMyStreamsBinding.initialize() {
        initializeContentLoader()
        initializeSwipeRefreshLayout()
        initializeMyStreamsRecyclerView()
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

    private fun bindActivityToolbar() {
        (requireActivity() as MainAppContentActivity).addToolbarNavigationOnClickListener(onToolbarBackPressed)
    }

    private fun unbindActivityToolbar() {
        (requireActivity() as MainAppContentActivity).removeToolbarNavigationOnClickListener(onToolbarBackPressed)
    }

    private fun FragmentMyStreamsBinding.initializeSwipeRefreshLayout() {
        swipeRefreshLayout.run {
            viewModel.isDataBeingRefreshed.observe(viewLifecycleOwner, ::setRefreshing)

            setOnRefreshListener(viewModel::refreshData)
        }
    }

    private fun FragmentMyStreamsBinding.initializeMyStreamsRecyclerView() {
        myStreamsRecyclerView.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            val myStreamsAdapter = MyStreamsAdapter(myStreamsDiffUtilItemCallback, imageLoader)

            adapter = myStreamsAdapter

            viewModel.myStreams.observe(viewLifecycleOwner, myStreamsAdapter::submitList)
        }
    }
}