package tv.wfc.livestreamsales.features.livebroadcast.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.livebroadcast.di.scope.LiveBroadcastFeatureScope
import tv.wfc.livestreamsales.features.livebroadcast.repository.BroadcastAnalyticsRepository
import tv.wfc.livestreamsales.features.livebroadcast.repository.IBroadcastAnalyticsRepository

@Module
abstract class RepositoryModule {
    @Binds
    @LiveBroadcastFeatureScope
    internal abstract fun provideBroadcastAnalyticsRepository(
        broadcastAnalyticsRepository: BroadcastAnalyticsRepository
    ): IBroadcastAnalyticsRepository
}