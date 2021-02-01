package com.example.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput

import androidx.fragment.app.Fragment
import com.example.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.modules.viewmodel.PhoneNumberInputViewModelModule
import com.example.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.qualifiers.PhoneNumberInputFragment
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