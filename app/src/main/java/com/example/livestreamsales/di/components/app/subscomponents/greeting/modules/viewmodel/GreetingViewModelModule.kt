package com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.mapkeys.ViewModelKey
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.greeting.GreetingViewModel
import com.example.livestreamsales.viewmodels.greeting.IGreetingViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class GreetingViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        fun provideIGreetingViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProviderFactory
        ): IGreetingViewModel{
            return ViewModelProvider(activity, viewModelProviderFactory)[GreetingViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(GreetingViewModel::class)
    abstract fun bindGreetingViewModelIntoMap(
        greetingViewModel: GreetingViewModel
    ): ViewModel
}