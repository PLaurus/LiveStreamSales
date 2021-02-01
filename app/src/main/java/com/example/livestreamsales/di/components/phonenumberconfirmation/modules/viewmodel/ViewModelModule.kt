package com.example.livestreamsales.di.components.phonenumberconfirmation.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.components.phonenumberconfirmation.qualifiers.PhoneNumberConfirmationFragment
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.phonenumberconfirmation.IPhoneNumberConfirmationViewModel
import com.example.livestreamsales.viewmodels.phonenumberconfirmation.PhoneNumberConfirmationViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @FragmentScope
    @Provides
    fun providePhoneNumberConfirmationViewModel(
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