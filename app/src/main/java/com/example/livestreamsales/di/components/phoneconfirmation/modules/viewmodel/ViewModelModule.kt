package com.example.livestreamsales.di.components.phoneconfirmation.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.components.phoneconfirmation.qualifiers.PhoneConfirmationFragment
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.phoneconfirmation.IPhoneConfirmationViewModel
import com.example.livestreamsales.viewmodels.phoneconfirmation.PhoneConfirmationViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @FragmentScope
    @Provides
    fun provideTelephoneNumberConfirmationViewModel(
        @PhoneConfirmationFragment
        fragment: Fragment,
        viewModelProviderFactory: ViewModelProviderFactory
    ): IPhoneConfirmationViewModel{
        return ViewModelProvider(
            fragment,
            viewModelProviderFactory
        )[PhoneConfirmationViewModel::class.java]
    }
}