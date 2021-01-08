package com.example.livestreamsales.di.components.telephonenumberinput

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.viewmodels.ViewModelProviderFactory
import com.example.livestreamsales.viewmodels.telephonenumberinput.ITelephoneNumberInputViewModel
import com.example.livestreamsales.viewmodels.telephonenumberinput.TelephoneNumberInputViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ViewModelModule {

    @FragmentScope
    @Provides
    fun provideTelephoneNumberInputViewModel(
        @Named(TelephoneNumberInputComponent.DEPENDENCY_NAME_TELEPHONE_NUMBER_INPUT_FRAGMENT)
        fragment: Fragment,
        viewModelProviderFactory: ViewModelProviderFactory
    ): ITelephoneNumberInputViewModel{
        return ViewModelProvider(
            fragment,
            viewModelProviderFactory
        )[TelephoneNumberInputViewModel::class.java]
    }
}