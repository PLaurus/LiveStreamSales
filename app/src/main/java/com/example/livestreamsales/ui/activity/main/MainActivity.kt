package com.example.livestreamsales.ui.activity.main

import android.os.Bundle
import androidx.navigation.findNavController
import com.example.livestreamsales.NavigationGraphRootDirections
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.ActivityMainBinding
import com.example.livestreamsales.di.components.main.MainComponent
import com.example.livestreamsales.ui.activity.base.BaseActivity
import com.example.livestreamsales.viewmodels.IMainViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : BaseActivity() {
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
        mainComponent = appComponent.mainActivityComponent().create(this)
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