package tv.wfc.livestreamsales.ui.activity.login

import android.os.Bundle
import androidx.navigation.findNavController
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.ActivityAuthorizationBinding
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.LogInComponent
import tv.wfc.livestreamsales.ui.activity.base.BaseActivity
import tv.wfc.livestreamsales.viewmodels.login.ILogInViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable
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
        initializeAuthorizationComponent()
        injectDependencies()
        super.onCreate(savedInstanceState)
        bindView()
        observeResponseErrors()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initializeAuthorizationComponent(){
        logInComponent = appComponent.authorizationComponent().create(this)
    }

    private fun injectDependencies(){
        logInComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
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