package tv.wfc.livestreamsales.features.livebroadcast.storage.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsStorage
import tv.wfc.livestreamsales.features.rest.api.authorized.IBroadcastAnalyticsApi
import tv.wfc.livestreamsales.features.rest.model.api.request.NotifyWatchingBroadcastRequestBody
import javax.inject.Inject

class BroadcastAnalyticsRemoteStorage @Inject constructor(
    private val broadcastAnalyticsApi: IBroadcastAnalyticsApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastAnalyticsStorage {
    override fun notifyWatchingBroadcast(broadcastId: Long): Completable {
        val notifyWatchingBroadcastRequestBody = NotifyWatchingBroadcastRequestBody(broadcastId)

        return broadcastAnalyticsApi
            .notifyWatchingBroadcast(notifyWatchingBroadcastRequestBody)
            .ignoreElement()
            .subscribeOn(ioScheduler)
    }
}