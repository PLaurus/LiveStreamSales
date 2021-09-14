package tv.wfc.livestreamsales.application.storage.broadcastsinformation

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast

interface IBroadcastsStorage {
    fun getBroadcasts(): Single<List<Broadcast>>
    fun saveBroadcasts(broadcasts: List<Broadcast>): Completable
    fun getBroadcast(id: Long): Single<Broadcast>
    fun getBroadcastViewersCount(broadcastId: Long): Single<Int>
}