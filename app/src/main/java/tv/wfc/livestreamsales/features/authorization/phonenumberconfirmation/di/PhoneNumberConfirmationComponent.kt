package tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di.modules.viewmodel.PhoneNumberConfirmationViewModelModule
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di.qualifiers.PhoneNumberConfirmationFragment
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di.scope.PhoneNumberConfirmationFeatureScope

@PhoneNumberConfirmationFeatureScope
@Subcomponent(modules = [
    PhoneNumberConfirmationViewModelModule::class
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

    fun inject(fragment: tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.ui.PhoneNumberConfirmationFragment)
}