package tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberconfirmation

import androidx.fragment.app.Fragment
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberconfirmation.modules.viewmodel.ViewModelModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberconfirmation.qualifiers.PhoneNumberConfirmationFragment
import tv.wfc.livestreamsales.di.scopes.FragmentScope
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

    fun inject(fragment: tv.wfc.livestreamsales.ui.activity.login.fragments.phonenumberconfirmation.PhoneNumberConfirmationFragment)
}