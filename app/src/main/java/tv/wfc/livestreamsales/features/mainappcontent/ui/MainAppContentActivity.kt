package tv.wfc.livestreamsales.features.mainappcontent.ui

import android.os.Bundle
import androidx.navigation.findNavController
import tv.wfc.livestreamsales.NavigationGraphRootDirections
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.authorizeduser.ui.base.AuthorizedUserActivity
import tv.wfc.livestreamsales.features.mainappcontent.viewmodel.IMainAppContentViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.livestreamsales.databinding.ActivityMainAppContentBinding
import javax.inject.Inject

class MainAppContentActivity : AuthorizedUserActivity() {
    private val disposables = CompositeDisposable()
    private val navigationController by lazy{
        findNavController(R.id.main_navigation_host_fragment)
    }

    private lateinit var viewBinding: ActivityMainAppContentBinding

    lateinit var mainAppContentComponent: MainAppContentComponent
        private set

    @Inject
    lateinit var mainAppContentViewModel: IMainAppContentViewModel

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
        mainAppContentComponent = authorizedUserComponent.mainAppComponent().create(this)
    }

    private fun injectDependencies(){
        mainAppContentComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivityMainAppContentBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
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

    private fun navigateToAuthorization(){
        val action = NavigationGraphRootDirections.actionGlobalAuthorizationGraphDestination()
        navigationController.navigate(action)
        finish()
    }
}