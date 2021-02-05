package tv.wfc.livestreamsales.application.repository.broadcastsinformation

import io.reactivex.rxjava3.core.Observable
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation

interface IBroadcastsInformationRepository {
    fun getBroadcasts(): Observable<List<BroadcastBaseInformation>>
}