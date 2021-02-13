package tv.wfc.livestreamsales.features.livebroadcast.di.modules.storage

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.livebroadcast.di.modules.storage.qualifiers.BroadcastAnalyticsRemoteStorage
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsStorage

@Module
abstract class LiveBroadcastStorageModule {
    @Binds
    @BroadcastAnalyticsRemoteStorage
    internal abstract fun provideBroadcastAnalyticsRemoteStorage(
        broadcastAnalyticsRemoteStorage: tv.wfc.livestreamsales.features.livebroadcast.storage.remote.BroadcastAnalyticsRemoteStorage
    ): IBroadcastAnalyticsStorage
}