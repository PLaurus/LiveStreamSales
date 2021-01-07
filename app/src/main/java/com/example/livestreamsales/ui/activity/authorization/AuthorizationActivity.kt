package com.example.livestreamsales.ui.activity.authorization

import android.os.Bundle
import androidx.navigation.findNavController
import com.example.livestreamsales.R
import com.example.livestreamsales.databinding.ActivityAuthorizationBinding
import com.example.livestreamsales.ui.activity.base.BaseActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AuthorizationActivity: BaseActivity() {
    private val disposables = CompositeDisposable()

    private lateinit var viewBinding: ActivityAuthorizationBinding

    private val navigationController by lazy{
        findNavController(R.id.authorization_navigation_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun bindView(){
        viewBinding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}