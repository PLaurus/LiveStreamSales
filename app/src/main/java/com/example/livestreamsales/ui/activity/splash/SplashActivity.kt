package com.example.livestreamsales.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import com.example.livestreamsales.databinding.ActivitySplashBinding
import com.example.livestreamsales.di.components.app.subscomponents.splash.SplashComponent
import com.example.livestreamsales.ui.activity.authorization.AuthorizationActivity
import com.example.livestreamsales.ui.activity.base.BaseActivity
import com.example.livestreamsales.ui.activity.greeting.GreetingActivity
import com.example.livestreamsales.ui.activity.main.MainActivity
import com.example.livestreamsales.viewmodels.splash.ISplashViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class SplashActivity: BaseActivity() {
    private val disposables = CompositeDisposable()

    private lateinit var viewBinding: ActivitySplashBinding

    lateinit var splashComponent: SplashComponent

    @Inject
    lateinit var viewModel: ISplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeSplashComponent()
        injectDependencies()
        super.onCreate(savedInstanceState)
        bindView()
        navigateToNextGraph()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initializeSplashComponent(){
        splashComponent = appComponent.splashComponent().create(this)
    }

    private fun injectDependencies(){
        splashComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun navigateToNextGraph(){
        viewModel.nextDestination.observe(this, { destination ->
            when(destination){
                ISplashViewModel.Destination.GREETING -> navigateToGreetingActivity()
                ISplashViewModel.Destination.AUTHORIZATION -> navigateToAuthorizationActivity()
                ISplashViewModel.Destination.MAIN -> navigateToMainActivity()
                else -> Unit
            }
        })
    }

    private fun navigateToGreetingActivity(){
        val greetingActivityIntent = Intent(applicationContext, GreetingActivity::class.java)
        startActivity(greetingActivityIntent)
        finish()
    }

    private fun navigateToMainActivity(){
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

    private fun navigateToAuthorizationActivity(){
        val authorizationActivity = Intent(applicationContext, AuthorizationActivity::class.java)
        startActivity(authorizationActivity)
        finish()
    }
}