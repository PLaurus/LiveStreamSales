package tv.wfc.livestreamsales.features.livebroadcast.repository

import io.reactivex.rxjava3.core.Completable
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.BroadcastAnalyticsRemoteStorage
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsStorage
import javax.inject.Inject

class BroadcastAnalyticsRepository @Inject constructor(
    @BroadcastAnalyticsRemoteStorage
    private val broadcastAnalyticsRemoteStorage: IBroadcastAnalyticsStorage
): IBroadcastAnalyticsRepository {
    override fun notifyWatchingBroadcast(broadcastId: Long): Completable {
        return broadcastAnalyticsRemoteStorage.notifyWatchingBroadcast(broadcastId)
    }
}