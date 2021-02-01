package tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput

import androidx.fragment.app.Fragment
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.modules.viewmodel.PhoneNumberInputViewModelModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.qualifiers.PhoneNumberInputFragment
import tv.wfc.livestreamsales.di.scopes.FragmentScope
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

    fun inject(fragment: tv.wfc.livestreamsales.ui.activity.login.fragments.phonenumberinput.PhoneNumberInputFragment)
}