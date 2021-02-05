package tv.wfc.livestreamsales.features.phonenumberinput.di

import androidx.fragment.app.Fragment
import tv.wfc.livestreamsales.features.phonenumberinput.di.modules.viewmodel.PhoneNumberInputViewModelModule
import tv.wfc.livestreamsales.features.phonenumberinput.di.qualifiers.PhoneNumberInputFragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.phonenumberinput.di.scope.PhoneNumberInputFeatureScope

@PhoneNumberInputFeatureScope
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

    fun inject(fragment: tv.wfc.livestreamsales.features.phonenumberinput.ui.PhoneNumberInputFragment)
}