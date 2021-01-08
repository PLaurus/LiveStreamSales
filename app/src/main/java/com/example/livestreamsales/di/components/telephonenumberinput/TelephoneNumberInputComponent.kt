package com.example.livestreamsales.di.components.telephonenumberinput

import androidx.fragment.app.Fragment
import com.example.livestreamsales.di.scopes.FragmentScope
import com.example.livestreamsales.ui.fragment.telephonenumberinput.TelephoneNumberInputFragment
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@FragmentScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface TelephoneNumberInputComponent {

    companion object{
        internal const val DEPENDENCY_NAME_TELEPHONE_NUMBER_INPUT_FRAGMENT="TELEPHONE_NUMBER_INPUT_FRAGMENT"
    }

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @Named(DEPENDENCY_NAME_TELEPHONE_NUMBER_INPUT_FRAGMENT)
            fragment: Fragment
        ): TelephoneNumberInputComponent
    }

    fun inject(fragment: TelephoneNumberInputFragment)
}