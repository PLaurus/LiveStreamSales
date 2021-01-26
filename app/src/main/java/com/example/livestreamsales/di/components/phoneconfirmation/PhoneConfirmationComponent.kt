package com.example.livestreamsales.di.components.phoneconfirmation

import androidx.fragment.app.Fragment
import com.example.livestreamsales.di.components.phoneconfirmation.modules.viewmodel.ViewModelModule
import com.example.livestreamsales.di.components.phoneconfirmation.qualifiers.PhoneConfirmationFragment
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.ui.fragment.telephonenumberconfirmation.TelephoneNumberConfirmationFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface PhoneConfirmationComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @PhoneConfirmationFragment
            fragment: Fragment
        ): PhoneConfirmationComponent
    }

    fun inject(fragment: TelephoneNumberConfirmationFragment)
}