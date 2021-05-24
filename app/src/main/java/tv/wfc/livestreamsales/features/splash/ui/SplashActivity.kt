package tv.wfc.livestreamsales.features.splash.ui

import android.content.Intent
import android.os.Bundle
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.livestreamsales.application.ui.base.BaseActivity
import tv.wfc.livestreamsales.databinding.ActivitySplashBinding
import tv.wfc.livestreamsales.features.greeting.ui.GreetingActivity
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import tv.wfc.livestreamsales.features.splash.di.SplashComponent
import tv.wfc.livestreamsales.features.splash.viewmodel.ISplashViewModel
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
                ISplashViewModel.Destination.MAIN_APP_CONTENT -> navigateToMainAppContentActivity()
                else -> Unit
            }
        })
    }

    private fun navigateToGreetingActivity(){
        val greetingActivityIntent = Intent(applicationContext, GreetingActivity::class.java)
        startActivity(greetingActivityIntent)
        finish()
    }

    private fun navigateToMainAppContentActivity(){
        val mainAppContentActivityIntent = Intent(applicationContext, MainAppContentActivity::class.java)
        startActivity(mainAppContentActivityIntent)
        finish()
    }
}