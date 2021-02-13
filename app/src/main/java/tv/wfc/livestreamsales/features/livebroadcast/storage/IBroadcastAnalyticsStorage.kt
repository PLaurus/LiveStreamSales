package tv.wfc.livestreamsales.features.livebroadcast.storage

import io.reactivex.rxjava3.core.Completable

interface IBroadcastAnalyticsStorage {
    fun notifyWatchingBroadcast(broadcastId: Long): Completable
}