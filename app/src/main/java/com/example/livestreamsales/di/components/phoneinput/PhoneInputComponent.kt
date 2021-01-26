package com.example.livestreamsales.di.components.phoneinput

import androidx.fragment.app.Fragment
import com.example.livestreamsales.di.components.phoneinput.modules.viewmodel.PhoneInputViewModelModule
import com.example.livestreamsales.di.components.phoneinput.qualifiers.PhoneInputFragment
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.ui.fragment.telephonenumberinput.TelephoneNumberInputFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [
    PhoneInputViewModelModule::class
])
interface PhoneInputComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @PhoneInputFragment
            fragment: Fragment
        ): PhoneInputComponent
    }

    fun inject(fragment: TelephoneNumberInputFragment)
}