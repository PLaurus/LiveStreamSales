package com.example.livestreamsales.di.components.phonenumberinput

import androidx.fragment.app.Fragment
import com.example.livestreamsales.di.components.phonenumberinput.modules.viewmodel.PhoneNumberInputViewModelModule
import com.example.livestreamsales.di.components.phonenumberinput.qualifiers.PhoneNumberInputFragment
import com.example.livestreamsales.di.scopes.FragmentScope
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [
    PhoneNumberInputViewModelModule::class
])
interface PhoneNumberInputComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @PhoneNumberInputFragment
            fragment: Fragment
        ): PhoneNumberInputComponent
    }

    fun inject(fragment: com.example.livestreamsales.ui.fragment.phonenumberinput.PhoneNumberInputFragment)
}