package tv.wfc.livestreamsales.application.storage.broadcastsinformation

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation

interface IBroadcastsInformationStorage {
    fun getBroadcasts(): Single<List<BroadcastBaseInformation>>
    fun saveBroadcasts(broadcasts: List<BroadcastBaseInformation>): Completable
}