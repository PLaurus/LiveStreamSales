package com.example.livestreamsales.di.components.app.subscomponents.login.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.mapkeys.ViewModelKey
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.login.ILogInViewModel
import com.example.livestreamsales.viewmodels.login.LogInViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class LogInViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        internal fun provideILogInViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProviderFactory
        ): ILogInViewModel{
            return ViewModelProvider(
                activity,
                viewModelProviderFactory
            )[LogInViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(LogInViewModel::class)
    internal abstract fun bindLogInViewModelIntoMap(
        logInViewModel: LogInViewModel
    ): ViewModel
}