package tv.wfc.livestreamsales.features.mainappcontent.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.livestreamsales.NavigationGraphRootDirections
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.ActivityMainAppContentBinding
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserActivity
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainappcontent.viewmodel.IMainAppContentViewModel
import javax.inject.Inject

class MainAppContentActivity : AuthorizedUserActivity() {
    private val disposables = CompositeDisposable()
    private val navigationController by lazy{
        findNavController(R.id.mainAppContentNavigationHostFragment)
    }

    lateinit var viewBinding: ActivityMainAppContentBinding

    lateinit var mainAppContentComponent: MainAppContentComponent
        private set

    @Inject
    lateinit var mainAppContentViewModel: IMainAppContentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeMainActivityComponent()
        injectDependencies()
        super.onCreate(savedInstanceState)
        bindView()
        initializeViews()
        observeIsUserLoggedOut()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    fun hideToolbar(){
        viewBinding.appBarLayout.visibility = View.GONE
    }

    fun showToolbar(){
        viewBinding.appBarLayout.visibility = View.VISIBLE
    }

    fun setToolbarTitle(title: String){
        viewBinding.toolbar.title = title
    }

    fun clearToolbarTitle(){
        viewBinding.toolbar.title = ""
    }

    private fun initializeMainActivityComponent(){
        mainAppContentComponent = authorizedUserComponent.mainAppComponent().create(this)
    }

    private fun injectDependencies(){
        mainAppContentComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivityMainAppContentBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun initializeViews(){
        initializeToolbar()
    }

    private fun observeIsUserLoggedOut(){
        mainAppContentViewModel.isUserLoggedIn.observe(this, { isUserLoggedIn ->
            manageLogOutNavigation(isUserLoggedIn)
        })
    }

    private fun manageLogOutNavigation(isUserLoggedIn: Boolean){
        if(!isUserLoggedIn){
            navigateToAuthorization()
        }
    }

    private fun initializeToolbar(){
        viewBinding.toolbar.apply {
            val appBarConfiguration = AppBarConfiguration(navigationController.graph)
            setupWithNavController(navigationController, appBarConfiguration)
        }
    }

    private fun navigateToAuthorization(){
        val action = NavigationGraphRootDirections.actionGlobalAuthorizationGraphDestination()
        navigationController.navigate(action)
        finish()
    }
}