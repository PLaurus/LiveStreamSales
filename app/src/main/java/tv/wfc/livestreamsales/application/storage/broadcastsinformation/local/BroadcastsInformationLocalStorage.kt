package tv.wfc.livestreamsales.application.storage.broadcastsinformation.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage
import javax.inject.Inject

class BroadcastsInformationLocalStorage @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastsInformationStorage {
    private val broadcasts = mutableListOf<BroadcastBaseInformation>()

    override fun getBroadcasts(): Single<List<BroadcastBaseInformation>> {
        return Single
            .just(broadcasts.toList())
            .subscribeOn(ioScheduler)
    }

    override fun saveBroadcasts(broadcasts: List<BroadcastBaseInformation>): Completable {
        return Completable
            .fromCallable {
                synchronized(this.broadcasts){
                    this.broadcasts.clear()
                    this.broadcasts.addAll(broadcasts)
                }
            }
            .subscribeOn(ioScheduler)
    }
}