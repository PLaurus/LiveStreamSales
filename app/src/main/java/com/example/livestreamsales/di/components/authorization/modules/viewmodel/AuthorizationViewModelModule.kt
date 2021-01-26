package com.example.livestreamsales.di.components.authorization.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.authorization.AuthorizationViewModel
import com.example.livestreamsales.viewmodels.authorization.IAuthorizationViewModel
import dagger.Module
import dagger.Provides

@Module
class AuthorizationViewModelModule {
    @ActivityScope
    @Provides
    fun provideAuthorizationViewModel(
        viewModelProviderFactory: ViewModelProviderFactory,
        activity: AppCompatActivity
    ): IAuthorizationViewModel{
        return ViewModelProvider(activity, viewModelProviderFactory)[AuthorizationViewModel::class.java]
    }
}