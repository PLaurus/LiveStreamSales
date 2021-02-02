package tv.wfc.livestreamsales.repository.broadcastsinformation

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.model.application.broadcastinformation.BroadcastBaseInformation

interface IBroadcastsInformationRepository {
    fun getBroadcasts(): Observable<List<BroadcastBaseInformation>>
}