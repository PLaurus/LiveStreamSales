package tv.wfc.livestreamsales.ui.activity.main

import android.os.Bundle
import androidx.navigation.findNavController
import tv.wfc.livestreamsales.NavigationGraphRootDirections
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.ActivityMainBinding
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.MainComponent
import tv.wfc.livestreamsales.ui.activity.base.AuthorizedUserActivity
import tv.wfc.livestreamsales.viewmodels.main.IMainViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AuthorizedUserActivity() {
    private val disposables = CompositeDisposable()
    private val navigationController by lazy{
        findNavController(R.id.main_navigation_host_fragment)
    }

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var mainComponent: MainComponent

    @Inject
    lateinit var mainViewModel: IMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeMainActivityComponent()
        injectDependencies()
        super.onCreate(savedInstanceState)
        bindView()
        observeIsUserLoggedOut()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun initializeMainActivityComponent(){
        mainComponent = authorizedUserComponent.mainComponent().create(this)
    }

    private fun injectDependencies(){
        mainComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun observeIsUserLoggedOut(){
        mainViewModel.isUserLoggedIn.observe(this, { isUserLoggedIn ->
            manageLogOutNavigation(isUserLoggedIn)
        })
    }

    private fun manageLogOutNavigation(isUserLoggedIn: Boolean){
        if(!isUserLoggedIn){
            navigateToAuthorization()
        }
    }

    private fun navigateToAuthorization(){
        val action = NavigationGraphRootDirections.actionGlobalAuthorizationGraphDestination()
        navigationController.navigate(action)
        finish()
    }
}