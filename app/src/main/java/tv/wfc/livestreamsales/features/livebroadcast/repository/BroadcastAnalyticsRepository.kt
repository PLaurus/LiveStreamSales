package tv.wfc.livestreamsales.features.livebroadcast.repository

import io.reactivex.rxjava3.core.Completable
import tv.wfc.livestreamsales.application.di.modules.datasource.qualifiers.BroadcastAnalyticsRemoteDataStore
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsDataStore
import javax.inject.Inject

class BroadcastAnalyticsRepository @Inject constructor(
    @BroadcastAnalyticsRemoteDataStore
    private val broadcastAnalyticsRemoteDataStore: IBroadcastAnalyticsDataStore
): IBroadcastAnalyticsRepository {
    override fun notifyWatchingBroadcast(broadcastId: Long): Completable {
        return broadcastAnalyticsRemoteDataStore.notifyWatchingBroadcast(broadcastId)
    }
}