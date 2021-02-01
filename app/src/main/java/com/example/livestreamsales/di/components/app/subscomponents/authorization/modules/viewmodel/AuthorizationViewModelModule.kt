package com.example.livestreamsales.di.components.app.subscomponents.authorization.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.mapkeys.ViewModelKey
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.authorization.AuthorizationViewModel
import com.example.livestreamsales.viewmodels.authorization.IAuthorizationViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class AuthorizationViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        internal fun provideIAuthorizationViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProviderFactory
        ): IAuthorizationViewModel{
            return ViewModelProvider(
                activity,
                viewModelProviderFactory
            )[AuthorizationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    internal abstract fun bindAuthorizationViewModelIntoMap(
        authorizationViewModel: AuthorizationViewModel
    ): ViewModel
}