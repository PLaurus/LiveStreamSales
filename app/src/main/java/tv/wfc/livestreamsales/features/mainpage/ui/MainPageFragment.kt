package tv.wfc.livestreamsales.features.mainpage.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.laurus.p.tools.viewpager2.onPageSelected
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast
import tv.wfc.livestreamsales.databinding.FragmentMainPageBinding
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserFragment
import tv.wfc.livestreamsales.features.home.ui.HomeFragmentDirections
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.mainpage.ui.adapters.announcements.AnnouncementsAdapter
import tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast.LiveBroadcastAdapter
import tv.wfc.livestreamsales.features.mainpage.viewmodel.IMainPageViewModel
import javax.inject.Inject

class MainPageFragment: AuthorizedUserFragment(R.layout.fragment_main_page) {
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
    override lateinit var viewModel: IMainPageViewModel

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var broadcastsDiffUtilCallback: DiffUtil.ItemCallback<Broadcast>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeMainPageComponent()
        injectDependencies()
    }

    override fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onContentViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDataIsPrepared() {
        super.onDataIsPrepared()
        initializeSwipeRefreshLayout()
        initializeNoLiveBroadcastsText()
        initializeLiveBroadcastTitleText()
        initializeDescriptionBackground()
        initializeLiveBroadcastsViewPager()
        initializeLiveBroadcastsPageIndicator()
        initializeNoAnnouncementsText()
        initializeAnnouncementsViewPager()
        initializeAnnouncementsPageIndicator()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindView()
    }

    private fun bindView(view: View){
        viewBinding = FragmentMainPageBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeMainPageComponent(){
        mainPageComponent = authorizedUserComponent.mainPageComponent().create(this)
    }

    private fun injectDependencies(){
        mainPageComponent.inject(this)
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

    private fun initializeLiveBroadcastTitleText(){
        viewBinding?.apply{
            viewModel.liveBroadcasts.observe(viewLifecycleOwner,{ liveBroadcasts ->
                if(liveBroadcasts.isNotEmpty()){
                    changeLiveBroadcastTitleText(liveBroadcastsPager.currentItem)
                    liveBroadcastsTitle.visibility = View.VISIBLE
                } else {
                    liveBroadcastsTitle.visibility = View.GONE
                }
            })

            liveBroadcastsPager.onPageSelected {
                changeLiveBroadcastTitleText(it)
            }
        }
    }

    private fun changeLiveBroadcastTitleText(pagePosition: Int){
        viewBinding
            ?.liveBroadcastsTitle
            ?.text = viewModel.getLiveBroadcastTitleByPosition(pagePosition)
    }

    private fun initializeDescriptionBackground(){
        viewBinding?.descriptionBackground?.apply{
            viewModel.liveBroadcasts.observe(viewLifecycleOwner,{ liveBroadcasts ->
                visibility = if(liveBroadcasts.isNotEmpty()){
                    View.VISIBLE
                } else View.GONE
            })
        }
    }

    private fun initializeLiveBroadcastsViewPager(){
        viewBinding?.liveBroadcastsPager?.apply{
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            adapter = liveBroadcastsAdapter
        }

        viewModel.liveBroadcasts.observe(viewLifecycleOwner,{ liveBroadcasts ->
            liveBroadcastsAdapter.submitList(liveBroadcasts)
        })
    }

    private fun initializeLiveBroadcastsPageIndicator(){
        viewBinding?.liveBroadcastsPager?.let {
            viewBinding?.liveBroadcastsPageIndicator?.setViewPager2(it)
        }
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

    private fun initializeAnnouncementsViewPager(){
        viewBinding?.announcementViewPager?.apply{
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            adapter = announcementsAdapter
        }

        viewModel.announcements.observe(viewLifecycleOwner,{ broadcastAnnouncements ->
            announcementsAdapter.submitList(broadcastAnnouncements)
        })
    }

    private fun initializeAnnouncementsPageIndicator(){
        viewBinding?.announcementViewPager?.let {
            viewBinding?.announcementPageIndicator?.setViewPager2(it)
        }
    }

    private fun navigateToLiveBroadcastDestination(liveBroadcastId: Long){
        val action = HomeFragmentDirections.actionHomeDestinationToLiveBroadcastDestination(liveBroadcastId)
        mainAppContentNavigationController.navigate(action)
    }
}