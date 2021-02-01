package com.example.livestreamsales.di.components.phonenumberinput.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.components.phonenumberinput.qualifiers.PhoneNumberInputFragment
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.phonenumberinput.IPhoneNumberInputViewModel
import com.example.livestreamsales.viewmodels.phonenumberinput.PhoneNumberInputViewModel
import dagger.Module
import dagger.Provides

@Module
class PhoneNumberInputViewModelModule {
    @FragmentScope
    @Provides
    fun providePhoneNumberInputViewModel(
        @PhoneNumberInputFragment
        fragment: Fragment,
        viewModelProviderFactory: ViewModelProviderFactory
    ): IPhoneNumberInputViewModel{
        return ViewModelProvider(
            fragment,
            viewModelProviderFactory
        )[PhoneNumberInputViewModel::class.java]
    }
}