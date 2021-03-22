package tv.wfc.livestreamsales.features.login.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.ui.base.BaseActivity
import tv.wfc.livestreamsales.databinding.ActivityAuthorizationBinding
import tv.wfc.livestreamsales.features.login.di.LogInComponent
import tv.wfc.livestreamsales.features.login.viewmodel.ILogInViewModel
import javax.inject.Inject

class LogInActivity: BaseActivity() {
    private val disposables = CompositeDisposable()

    private lateinit var viewBinding: ActivityAuthorizationBinding

    private val navigationController by lazy{
        findNavController(R.id.authorization_navigation_host_fragment)
    }

    lateinit var logInComponent: LogInComponent
        private set

    @Inject
    lateinit var viewModel: ILogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeLogInComponent()
        injectDependencies()
        super.onCreate(savedInstanceState)
        bindView()
        initializeViews()
        observeResponseErrors()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initializeLogInComponent(){
        logInComponent = appComponent.logInComponent().create(this)
    }

    private fun injectDependencies(){
        logInComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun initializeViews(){
        initializeToolbar()
    }

    private fun initializeToolbar(){
        viewBinding.toolbar.apply {
            val appBarConfiguration = AppBarConfiguration(navigationController.graph)
            setupWithNavController(navigationController, appBarConfiguration)
        }
    }
    
    private fun observeResponseErrors(){
        viewModel.responseError.observe(this, { responseError ->
            responseError?.message?.let{ message ->
                Snackbar.make(
                    viewBinding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }
}