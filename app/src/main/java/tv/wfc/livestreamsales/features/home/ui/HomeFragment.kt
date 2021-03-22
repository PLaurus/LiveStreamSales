package tv.wfc.livestreamsales.features.home.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.FragmentHomeBinding
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserFragment
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.home.ui.adapters.HomePagesAdapter
import tv.wfc.livestreamsales.features.home.viewmodel.IHomeViewModel
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import javax.inject.Inject

class HomeFragment: AuthorizedUserFragment(R.layout.fragment_home) {
    lateinit var homeComponent: HomeComponent
        private set

    @Inject
    lateinit var viewModel: IHomeViewModel

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

    private var viewBinding: FragmentHomeBinding? = null

    private val onPageSelectedFromViewPager = object: ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            val bottomNavigation = viewBinding?.bottomNavigation ?: return

            val title: String
            val menuItem: MenuItem

            when(position){
                0 -> {
                    title = resources.getString(R.string.main_page_title)
                    menuItem = bottomNavigation.menu.findItem(R.id.mainPage)
                }
                1 -> {
                    title = resources.getString(R.string.fragment_notifications_title)
                    menuItem = bottomNavigation.menu.findItem(R.id.notificationsPage)
                }
                2 -> {
                    title = resources.getString(R.string.fragment_categories_title)
                    menuItem = bottomNavigation.menu.findItem(R.id.categoriesPage)
                }
                3 -> {
                    title = resources.getString(R.string.fragment_settings_title)
                    menuItem = bottomNavigation.menu.findItem(R.id.settingsPage)
                }
                else -> return
            }

            (requireActivity() as MainAppContentActivity).setToolbarTitle(title)
            menuItem.isChecked = true
        }
    }

    private val onNavigationItemSelected = object : BottomNavigationView.OnNavigationItemSelectedListener{
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val viewPager = viewBinding?.viewPager ?: return false

            val title: String
            val currentPage: Int

            when(menuItem.itemId){
                R.id.mainPage -> {
                    title = resources.getString(R.string.main_page_title)
                    currentPage = 0
                }
                R.id.notificationsPage -> {
                    title = resources.getString(R.string.fragment_notifications_title)
                    currentPage = 1
                }
                R.id.categoriesPage -> {
                    title = resources.getString(R.string.fragment_categories_title)
                    currentPage = 2
                }
                R.id.settingsPage -> {
                    title = resources.getString(R.string.fragment_settings_title)
                    currentPage = 3
                }
                else -> return false
            }

            (requireActivity() as MainAppContentActivity).setToolbarTitle(title)
            viewPager.currentItem = currentPage

            return true
        }
    }

    private fun initializeHomeComponent(){
        homeComponent = authorizedUserComponent.homeComponent().create(this)
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
}