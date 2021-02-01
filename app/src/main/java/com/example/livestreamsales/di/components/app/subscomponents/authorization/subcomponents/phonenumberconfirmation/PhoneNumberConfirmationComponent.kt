package com.example.livestreamsales.di.components.app.subscomponents.authorization.subcomponents.phonenumberconfirmation

import androidx.fragment.app.Fragment
import com.example.livestreamsales.di.components.app.subscomponents.authorization.subcomponents.phonenumberconfirmation.modules.viewmodel.ViewModelModule
import com.example.livestreamsales.di.components.app.subscomponents.authorization.subcomponents.phonenumberconfirmation.qualifiers.PhoneNumberConfirmationFragment
import com.example.livestreamsales.di.scopes.FragmentScope
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface PhoneNumberConfirmationComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @PhoneNumberConfirmationFragment
            fragment: Fragment
        ): PhoneNumberConfirmationComponent
    }

    fun inject(fragment: com.example.livestreamsales.ui.fragment.phonenumberconfirmation.PhoneNumberConfirmationFragment)
}