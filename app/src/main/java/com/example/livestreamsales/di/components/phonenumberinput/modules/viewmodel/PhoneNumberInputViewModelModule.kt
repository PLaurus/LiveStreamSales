package com.example.livestreamsales.di.components.phonenumberinput.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.components.phonenumberinput.qualifiers.PhoneNumberInputFragment
import com.example.livestreamsales.di.mapkeys.ViewModelKey
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.phonenumberinput.IPhoneNumberInputViewModel
import com.example.livestreamsales.viewmodels.phonenumberinput.PhoneNumberInputViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class PhoneNumberInputViewModelModule {
    companion object{
        @FragmentScope
        @Provides
        @JvmStatic
        fun provideIPhoneNumberInputViewModel(
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

    @Binds
    @IntoMap
    @ViewModelKey(PhoneNumberInputViewModel::class)
    abstract fun bindPhoneNumberInputViewModelIntoMap(
        phoneNumberInputViewModel: PhoneNumberInputViewModel
    ): ViewModel
}