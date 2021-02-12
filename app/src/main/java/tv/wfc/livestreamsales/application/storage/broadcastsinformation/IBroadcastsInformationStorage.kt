package tv.wfc.livestreamsales.application.storage.broadcastsinformation

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation

interface IBroadcastsInformationStorage {
    fun getBroadcasts(): Single<List<BroadcastInformation>>
    fun saveBroadcasts(broadcasts: List<BroadcastInformation>): Completable
    fun getBroadcast(id: Long): Single<BroadcastInformation>
}