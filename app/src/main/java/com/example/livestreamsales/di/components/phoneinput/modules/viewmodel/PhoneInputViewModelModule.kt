package com.example.livestreamsales.di.components.phoneinput.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.components.phoneinput.qualifiers.PhoneInputFragment
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.phoneinput.IPhoneInputViewModel
import com.example.livestreamsales.viewmodels.phoneinput.PhoneInputViewModel
import dagger.Module
import dagger.Provides

@Module
class PhoneInputViewModelModule {
    @FragmentScope
    @Provides
    fun provideTelephoneNumberInputViewModel(
        @PhoneInputFragment
        fragment: Fragment,
        viewModelProviderFactory: ViewModelProviderFactory
    ): IPhoneInputViewModel{
        return ViewModelProvider(
            fragment,
            viewModelProviderFactory
        )[PhoneInputViewModel::class.java]
    }
}