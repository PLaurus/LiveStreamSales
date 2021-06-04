package tv.wfc.livestreamsales.features.usersettings.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.usersettings.di.modules.ProfileViewModelModule
import tv.wfc.livestreamsales.features.usersettings.di.scope.ProfileFeature
import tv.wfc.livestreamsales.features.usersettings.ui.ProfileFragment

@ProfileFeature
@Subcomponent(modules = [
    ProfileViewModelModule::class
])
interface ProfileComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            fragment: Fragment
        ): ProfileComponent
    }

    fun inject(fragment: ProfileFragment)
}