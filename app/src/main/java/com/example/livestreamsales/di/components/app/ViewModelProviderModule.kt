package com.example.livestreamsales.di.components.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.mapkeys.ViewModelKey
import com.example.livestreamsales.viewmodels.MainViewModel
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.authorization.AuthorizationViewModel
import com.example.livestreamsales.viewmodels.telephonenumberinput.TelephoneNumberInputViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(
        mainViewModel: MainViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    abstract fun provideAuthorizationViewModel(
        authorizationViewModel: AuthorizationViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TelephoneNumberInputViewModel::class)
    abstract fun provideTelephoneNumberInputViewModel(
        telephoneNumberInputViewModel: TelephoneNumberInputViewModel
    ): ViewModel

    @Binds
    internal abstract fun provideViewModelProviderFactory(
        viewModelProviderFactory: ViewModelProviderFactory
    ): ViewModelProvider.Factory
}