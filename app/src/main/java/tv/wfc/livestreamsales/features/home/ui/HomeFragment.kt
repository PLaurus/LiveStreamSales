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
import javax.inject.Inject

class HomeFragment: AuthorizedUserFragment(R.layout.fragment_home) {
    lateinit var homeComponent: HomeComponent
        private set

    @Inject
    override lateinit var viewModel: IHomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeHomeComponent()
        injectDependencies()
    }

    override fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onContentViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDataIsPrepared() {
        super.onDataIsPrepared()
        initializeViewPager()
        initializeBottomNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
    }

    private var viewBinding: FragmentHomeBinding? = null

    private val onPageSelectedFromViewPager = object: ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            val bottomNavigation = viewBinding?.bottomNavigation ?: return

            val menuItem = when(position){
                0 -> bottomNavigation.menu.findItem(R.id.mainPage)
                1 -> bottomNavigation.menu.findItem(R.id.notificationsPage)
                2 -> bottomNavigation.menu.findItem(R.id.categoriesPage)
                3 -> bottomNavigation.menu.findItem(R.id.settingsPage)
                else -> return
            }

            menuItem.isChecked = true
        }
    }

    private val onNavigationItemSelected = object : BottomNavigationView.OnNavigationItemSelectedListener{
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val viewPager = viewBinding?.viewPager ?: return false

            viewPager.currentItem = when(menuItem.itemId){
                R.id.mainPage -> 0
                R.id.notificationsPage -> 1
                R.id.categoriesPage -> 2
                R.id.settingsPage -> 3
                else -> return false
            }

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