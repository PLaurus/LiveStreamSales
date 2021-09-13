package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.modules.VideoStreamConstantsModule
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.modules.ViewModelModule
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.LiveBroadcastingSettingsFragment
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.scope.LiveBroadcastingSettingsFeatureScope

@LiveBroadcastingSettingsFeatureScope
@Subcomponent(modules = [
    VideoStreamConstantsModule::class,
    ViewModelModule::class
])
interface LiveBroadcastingSettingsComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @LiveBroadcastingSettingsFragment
            fragment: Fragment
        ): LiveBroadcastingSettingsComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.ui.LiveBroadcastingSettingsFragment)
}