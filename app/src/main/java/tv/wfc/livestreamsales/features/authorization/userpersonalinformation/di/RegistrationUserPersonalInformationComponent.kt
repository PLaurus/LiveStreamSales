package tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di.modules.viewmodel.RegistrationUserPersonalInformationViewModelModule
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di.qualifiers.RegistrationUserPersonalInformationFragment
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di.scope.RegistrationUserPersonalInformationFeatureScope

@RegistrationUserPersonalInformationFeatureScope
@Subcomponent(modules =[
    RegistrationUserPersonalInformationViewModelModule::class
])
interface RegistrationUserPersonalInformationComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @RegistrationUserPersonalInformationFragment
            fragment: Fragment
        ): RegistrationUserPersonalInformationComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.authorization.userpersonalinformation.ui.RegistrationUserPersonalInformationFragment)
}