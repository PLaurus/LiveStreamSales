package com.example.livestreamsales.di.components.phonenumberconfirmation.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.components.phonenumberconfirmation.qualifiers.PhoneNumberConfirmationFragment
import com.example.livestreamsales.di.mapkeys.ViewModelKey
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.phonenumberconfirmation.IPhoneNumberConfirmationViewModel
import com.example.livestreamsales.viewmodels.phonenumberconfirmation.PhoneNumberConfirmationViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    companion object{
        @FragmentScope
        @Provides
        @JvmStatic
        fun provideIPhoneNumberConfirmationViewModel(
            @PhoneNumberConfirmationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProviderFactory
        ): IPhoneNumberConfirmationViewModel{
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[PhoneNumberConfirmationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(PhoneNumberConfirmationViewModel::class)
    abstract fun bindPhoneNumberConfirmationViewModelIntoMap(
        phoneNumberConfirmationViewModel: PhoneNumberConfirmationViewModel
    ): ViewModel
}