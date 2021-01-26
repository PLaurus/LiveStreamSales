package com.example.livestreamsales.ui.activity.authorization

import android.os.Bundle
import androidx.navigation.findNavController
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.ActivityAuthorizationBinding
import com.example.livestreamsales.di.components.authorization.AuthorizationComponent
import com.example.livestreamsales.ui.activity.base.BaseActivity
import com.example.livestreamsales.viewmodels.authorization.IAuthorizationViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class AuthorizationActivity: BaseActivity() {
    private val disposables = CompositeDisposable()

    private lateinit var viewBinding: ActivityAuthorizationBinding

    private val navigationController by lazy{
        findNavController(R.id.authorization_navigation_host_fragment)
    }

    lateinit var authorizationComponent: AuthorizationComponent
        private set
    @Inject
    lateinit var viewModel: IAuthorizationViewModel

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
        authorizationComponent = appComponent.authorizationComponent().create(this)
    }

    private fun injectDependencies(){
        authorizationComponent.inject(this)
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