package tv.wfc.livestreamsales.features.settings.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.settings.di.modules.SettingsViewModelModule
import tv.wfc.livestreamsales.features.settings.di.scope.SettingsFeature
import tv.wfc.livestreamsales.features.settings.ui.SettingsFragment

@SettingsFeature
@Subcomponent(modules = [
    SettingsViewModelModule::class
])
interface SettingsComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            fragment: Fragment
        ): SettingsComponent
    }

    fun inject(fragment: SettingsFragment)
}