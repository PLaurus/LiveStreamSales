package com.example.livestreamsales.ui.activity.greeting

import android.os.Bundle
import com.example.livestreamsales.databinding.ActivityGreetingBinding
import com.example.livestreamsales.di.components.greeting.GreetingComponent
import com.example.livestreamsales.ui.activity.base.BaseActivity
import com.example.livestreamsales.viewmodels.greeting.IGreetingViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class GreetingActivity: BaseActivity() {
    private val disposables = CompositeDisposable()

    private lateinit var viewBinding: ActivityGreetingBinding

    lateinit var greetingComponent: GreetingComponent
        private set

    @Inject
    lateinit var viewModel: IGreetingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeGreetingComponent()
        injectDependencies()
        super.onCreate(savedInstanceState)
        bindView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initializeGreetingComponent(){
        greetingComponent = appComponent.greetingComponent().create(this)
    }

    private fun injectDependencies(){
        greetingComponent.inject(this)
    }

    private fun bindView(){
        viewBinding = ActivityGreetingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}