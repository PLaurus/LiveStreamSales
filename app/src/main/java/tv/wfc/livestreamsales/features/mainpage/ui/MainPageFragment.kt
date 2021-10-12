package tv.wfc.livestreamsales.features.mainpage.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.laurus.p.recyclerviewitemdecorators.GapBetweenItems
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentMainPageBinding
import tv.wfc.livestreamsales.features.home.ui.HomeFragmentDirections
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.mainpage.ui.adapters.announcements.AnnouncementsAdapter
import tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast.LiveBroadcastAdapter
import tv.wfc.livestreamsales.features.mainpage.viewmodel.IMainPageViewModel
import javax.inject.Inject

class MainPageFragment: BaseFragment(R.layout.fragment_main_page) {
    private val homeNavigationController by lazy{
        findNavController()
    }

    private val mainAppContentNavigationController by lazy{
        requireActivity().findNavController(R.id.mainAppContentNavigationHostFragment)
    }

    private val liveBroadcastsAdapter: LiveBroadcastAdapter by lazy{
        LiveBroadcastAdapter(
            broadcastsDiffUtilCallback,
            imageLoader,
            ::navigateToLiveBroadcastDestination)
    }

    private val announcementsAdapter: AnnouncementsAdapter by lazy{
        AnnouncementsAdapter(broadcastsDiffUtilCallback, imageLoader)
    }

    private var viewBinding: FragmentMainPageBinding? = null

    private lateinit var mainPageComponent: MainPageComponent

    @Inject
    lateinit var viewModel: IMainPageViewModel

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var broadcastsDiffUtilCallback: DiffUtil.ItemCallback<Broadcast>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeMainPageComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindView()
    }

    private fun initializeMainPageComponent(){
        mainPageComponent = appComponent.mainPageComponent().create(this)
    }

    private fun injectDependencies(){
        mainPageComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentMainPageBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeContentLoader(){
        viewBinding?.contentLoader?.apply {
            clearPreparationListeners()
            attachViewModel(viewLifecycleOwner, viewModel)
            addOnDataIsPreparedListener(::onDataIsPrepared)
        }
    }

    private fun onDataIsPrepared() {
        initializeSwipeRefreshLayout()
        initializeNoLiveBroadcastsText()
        initializeLiveBroadcastsRecyclerView()
        initializeNoAnnouncementsText()
        initializeAnnouncementsRecyclerView()
    }

    private fun initializeSwipeRefreshLayout(){
        viewBinding?.swipeRefreshLayout?.apply{
            setOnRefreshListener {
                viewModel.refreshData()
            }

            viewModel.isDataBeingRefreshed.observe(viewLifecycleOwner,{ isDataBeingRefreshed ->
                isRefreshing = isDataBeingRefreshed
            })
        }
    }

    private fun initializeNoLiveBroadcastsText(){
        viewBinding?.noLiveBroadcastsText?.apply{
            viewModel.liveBroadcasts.observe(viewLifecycleOwner,{ liveBroadcasts ->
                visibility = if(liveBroadcasts.isEmpty()){
                    View.VISIBLE
                } else View.GONE
            })
        }
    }

    private fun initializeLiveBroadcastsRecyclerView(){
        viewBinding?.liveBroadcastsRecyclerView?.apply{
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            adapter = liveBroadcastsAdapter

            val contentMargin = resources.getDimensionPixelSize(R.dimen.contentMargin_default)
            addItemDecoration(GapBetweenItems(contentMargin))
        }

        viewModel.liveBroadcasts.observe(viewLifecycleOwner,{ liveBroadcasts ->
            liveBroadcastsAdapter.submitList(liveBroadcasts)
        })
    }

    private fun initializeNoAnnouncementsText(){
        viewBinding?.noAnnouncementsText?.apply{
            viewModel.announcements.observe(viewLifecycleOwner,{ announcements ->
                visibility = if(announcements.isEmpty()){
                    View.VISIBLE
                } else View.GONE
            })
        }
    }

    private fun initializeAnnouncementsRecyclerView(){
        viewBinding?.announcementsRecyclerView?.apply{
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            adapter = announcementsAdapter

            val contentMargin = resources.getDimensionPixelSize(R.dimen.contentMargin_default)
            addItemDecoration(GapBetweenItems(contentMargin))
        }

        viewModel.announcements.observe(viewLifecycleOwner,{ broadcastAnnouncements ->
            announcementsAdapter.submitList(broadcastAnnouncements)
        })
    }

    private fun navigateToLiveBroadcastDestination(liveBroadcastId: Long){
        val action = HomeFragmentDirections.actionHomeDestinationToLiveBroadcastDestination(liveBroadcastId)
        mainAppContentNavigationController.navigate(action)
    }
}