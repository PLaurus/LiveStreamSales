package tv.wfc.livestreamsales.features.livebroadcast.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.api.LiveBroadcastApiModule
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.exoplayer.ExoPlayerModule
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.repository.RepositoryModule
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.storage.StorageModule
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.viewmodel.LiveBroadcastViewModelModule
import tv.wfc.livestreamsales.features.livebroadcast.di.qualifiers.LiveBroadcastFragment
import tv.wfc.livestreamsales.features.livebroadcast.di.scope.LiveBroadcastFeatureScope

@LiveBroadcastFeatureScope
@Subcomponent(modules = [
    LiveBroadcastViewModelModule::class,
    ExoPlayerModule::class,
    LiveBroadcastApiModule::class,
    StorageModule::class,
    RepositoryModule::class
])
interface LiveBroadcastComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @LiveBroadcastFragment
            fragment: Fragment
        ): LiveBroadcastComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.livebroadcast.ui.LiveBroadcastFragment)
}