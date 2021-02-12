package tv.wfc.livestreamsales.application.repository.broadcastsinformation

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation

interface IBroadcastsInformationRepository {
    fun getBroadcasts(): Observable<List<BroadcastInformation>>
    fun getBroadcast(id: Long): Single<BroadcastInformation>
}