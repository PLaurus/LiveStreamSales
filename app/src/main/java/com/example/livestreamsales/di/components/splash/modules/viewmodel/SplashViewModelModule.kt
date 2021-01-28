package com.example.livestreamsales.di.components.splash.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.mapkeys.ViewModelKey
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.splash.ISplashViewModel
import com.example.livestreamsales.viewmodels.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class SplashViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        internal fun provideISplashViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProviderFactory
        ): ISplashViewModel{
            return ViewModelProvider(activity, viewModelProviderFactory)[SplashViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModelIntoMap(
        splashViewModel: SplashViewModel
    ): ViewModel
}