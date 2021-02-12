package tv.wfc.livestreamsales.application.storage.broadcastsinformation.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage
import javax.inject.Inject

class BroadcastsInformationLocalStorage @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastsInformationStorage {
    private val broadcasts = mutableListOf<BroadcastInformation>()

    override fun getBroadcasts(): Single<List<BroadcastInformation>> {
        return Single
            .fromCallable { broadcasts.toList() }
            .subscribeOn(ioScheduler)
    }

    override fun saveBroadcasts(broadcasts: List<BroadcastInformation>): Completable {
        return Completable
            .fromCallable {
                synchronized(this.broadcasts){
                    this.broadcasts.clear()
                    this.broadcasts.addAll(broadcasts)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getBroadcast(id: Long): Single<BroadcastInformation> {
        return Single
            .fromCallable {
                broadcasts.first { it.id == id }
            }
            .subscribeOn(ioScheduler)
    }
}