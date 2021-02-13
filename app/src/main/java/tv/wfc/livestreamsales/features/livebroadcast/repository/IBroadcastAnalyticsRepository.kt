package tv.wfc.livestreamsales.features.livebroadcast.repository

import io.reactivex.rxjava3.core.Completable

interface IBroadcastAnalyticsRepository {
    fun notifyWatchingBroadcast(broadcastId: Long): Completable
}