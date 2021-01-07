package com.example.livestreamsales.ui.activity.splash

import android.os.Bundle
import androidx.navigation.findNavController
import com.example.livestreamsales.NavigationGraphSplashDirections
import com.example.livestreamsales.R
import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.databinding.ActivitySplashBinding
import com.example.livestreamsales.di.components.app.ReactiveXModule
import com.example.livestreamsales.di.components.splash.SplashComponent
import com.example.livestreamsales.ui.activity.base.BaseActivity
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject
import javax.inject.Named

class SplashScreenActivity: BaseActivity() {
    private val disposables = CompositeDisposable()
    private val navigationController by lazy {
        findNavController(R.id.splash_navigation_host_fragment)
    }

    private lateinit var viewBinding: ActivitySplashBinding

    lateinit var splashComponent: SplashComponent
    @Inject
    lateinit var authorizationManager: IAuthorizationManager

    @Inject
    @Named(ReactiveXModule.DEPENDENCY_NAME_MAIN_THREAD_SCHEDULER)
    lateinit var mainThreadScheduler: Scheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        getDependencies()
        super.onCreate(savedInstanceState)
        bindView()
        navigateToNextGraph()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun getDependencies(){
        splashComponent = appComponent.splashComponent().create(this)
        splashComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun navigateToNextGraph(){
        authorizationManager.isUserLoggedIn
            .subscribeOn(mainThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe{ isUserLoggedIn ->
                if(isUserLoggedIn){
                    navigateToMainGraph()
                } else{
                    navigateToAuthorizationGraph()
                }
            }
            .addTo(disposables)
    }

    private fun navigateToMainGraph(){
        val action = NavigationGraphSplashDirections.actionGlobalMainGraphDestination()
        navigationController.navigate(action)
        finish()
    }

    private fun navigateToAuthorizationGraph(){
        val action = NavigationGraphSplashDirections.actionGlobalAuthorizationGraphDestination()
        navigationController.navigate(action)
        finish()
    }
}