package tv.wfc.livestreamsales.application.repository.broadcastsinformation

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast

interface IBroadcastsInformationRepository {
    fun getBroadcasts(): Observable<List<Broadcast>>
    fun getBroadcast(id: Long): Single<Broadcast>
}