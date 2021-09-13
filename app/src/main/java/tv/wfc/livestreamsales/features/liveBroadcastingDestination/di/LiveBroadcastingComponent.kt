package tv.wfc.livestreamsales.features.liveBroadcastingDestination.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.modules.viewModel.ViewModelModule
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.qualifiers.LiveBroadcastingFragment
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.scope.LiveBroadcastingFeatureScope

@LiveBroadcastingFeatureScope
@Subcomponent(modules = [
    ViewModelModule::class
])
interface LiveBroadcastingComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @LiveBroadcastingFragment
            fragment: Fragment
        ): LiveBroadcastingComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.liveBroadcastingDestination.ui.LiveBroadcastingFragment)
}