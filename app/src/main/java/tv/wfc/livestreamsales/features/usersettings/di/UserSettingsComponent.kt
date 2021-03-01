package tv.wfc.livestreamsales.features.usersettings.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.usersettings.di.modules.UserSettingsViewModelModule
import tv.wfc.livestreamsales.features.usersettings.di.scope.UserSettingsFeature
import tv.wfc.livestreamsales.features.usersettings.ui.UserSettingsFragment

@UserSettingsFeature
@Subcomponent(modules = [
    UserSettingsViewModelModule::class
])
interface UserSettingsComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            fragment: Fragment
        ): UserSettingsComponent
    }

    fun inject(fragment: UserSettingsFragment)
}