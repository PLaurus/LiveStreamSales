package tv.wfc.livestreamsales.features.livebroadcast.storage

import io.reactivex.rxjava3.core.Completable

interface IBroadcastAnalyticsDataStore {
    fun notifyWatchingBroadcast(broadcastId: Long): Completable
}