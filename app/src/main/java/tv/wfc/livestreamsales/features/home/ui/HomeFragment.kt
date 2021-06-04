package tv.wfc.livestreamsales.features.home.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.NavigationGraphRootDirections
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentHomeBinding
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.home.ui.adapters.HomePagesAdapter
import tv.wfc.livestreamsales.features.home.viewmodel.IHomeViewModel
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment: BaseFragment(R.layout.fragment_home) {
    private val navigationController by lazy { findNavController() }

    private var viewBinding: FragmentHomeBinding? = null
    private var activePageIndex = 0

    lateinit var homeComponent: HomeComponent
        private set

    @Inject
    lateinit var viewModel: IHomeViewModel

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeHomeComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    fun navigateToMainPage(){
        navigateToPage(0)
    }

    fun navigateToNotificationsPage(){
        navigateToPage(1)
    }

    fun navigateToMyOrdersPage(){
        if(viewModel.isUserAuthorized.value == true){
            navigateToPage(2)
        } else{
            navigateToPage(activePageIndex)
            navigateToPhoneNumberInputDestination()
        }
    }

    fun navigateToProfilePage(){
        if(viewModel.isUserAuthorized.value == true){
            navigateToPage(3)
        } else{
            navigateToPage(activePageIndex)
            navigateToPhoneNumberInputDestination()
        }
    }

    private val onPageSelectedFromViewPager = object: ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            when(position){
                0 -> navigateToMainPage()
                1 -> navigateToNotificationsPage()
                2 -> navigateToMyOrdersPage()
                3 -> navigateToProfilePage()
                else -> return
            }
        }
    }

    private val onNavigationItemSelected = object : BottomNavigationView.OnNavigationItemSelectedListener{
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            when(menuItem.itemId){
                R.id.mainPage -> navigateToMainPage()
                R.id.notificationsPage -> navigateToNotificationsPage()
                R.id.myOrdersPage -> navigateToMyOrdersPage()
                R.id.profilePage -> navigateToProfilePage()
                else -> return false
            }

            return true
        }
    }

    private fun initializeHomeComponent(){
        homeComponent = appComponent.homeComponent().create(this)
    }

    private fun injectDependencies(){
        homeComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentHomeBinding.bind(view)
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
        initializeViewPager()
        initializePromoBanner()
        initializeBottomNavigation()
    }

    private fun initializeViewPager(){
        viewBinding?.viewPager?.apply{
            isUserInputEnabled = false
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            adapter = HomePagesAdapter(this@HomeFragment)
            registerOnPageChangeCallback(onPageSelectedFromViewPager)
        }
    }

    private fun initializeBottomNavigation(){
        viewBinding?.bottomNavigation?.setOnNavigationItemSelectedListener(onNavigationItemSelected)
    }

    private fun initializePromoBanner(){
        viewModel.isUserAuthorized.observe(viewLifecycleOwner, { isUserAuthorized ->
            if(isUserAuthorized) hidePromoBanner() else showPromoBanner()
        })

        viewBinding?.promoLayout?.run {
            clicks()
                .observeOn(mainThreadScheduler)
                .throttleLatest(500L, TimeUnit.MILLISECONDS)
                .subscribeBy(
                    onNext = { navigateToPhoneNumberInputDestination() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun showPromoBanner(){
        viewBinding?.promoLayout?.visibility = View.VISIBLE
    }

    private fun hidePromoBanner(){
        viewBinding?.promoLayout?.visibility = View.GONE
    }

    private fun navigateToPhoneNumberInputDestination(){
        val action = NavigationGraphRootDirections.actionGlobalPhoneNumberInputDestination()
        navigationController.navigate(action)
    }

    private fun navigateToPage(pagePosition: Int){
        viewBinding?.run{
            val menuItemId: Int
            val title: String

            when(pagePosition){
                0 -> {
                    menuItemId = R.id.mainPage
                    title = resources.getString(R.string.main_page_title)
                }
                1 -> {
                    menuItemId = R.id.notificationsPage
                    title = resources.getString(R.string.fragment_notifications_title)
                }
                2 -> {
                    menuItemId = R.id.myOrdersPage
                    title = resources.getString(R.string.fragment_my_orders_title)
                }
                else -> {
                    menuItemId = R.id.profilePage
                    title = resources.getString(R.string.fragment_profile_destination_title)
                }
            }

            activePageIndex = pagePosition
            viewPager.currentItem = pagePosition
            bottomNavigation.menu.findItem(menuItemId).isChecked = true
            (requireActivity() as MainAppContentActivity).setToolbarTitle(title)
        }
    }
}