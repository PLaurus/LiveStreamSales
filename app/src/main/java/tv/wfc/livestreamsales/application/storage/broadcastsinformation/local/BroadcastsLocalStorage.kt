package tv.wfc.livestreamsales.application.storage.broadcastsinformation.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsStorage
import javax.inject.Inject

class BroadcastsLocalStorage @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastsStorage {
    private val broadcasts = mutableListOf<Broadcast>()

    override fun getBroadcasts(): Single<List<Broadcast>> {
        return Single
            .fromCallable { broadcasts.toList() }
            .subscribeOn(ioScheduler)
    }

    override fun saveBroadcasts(broadcasts: List<Broadcast>): Completable {
        return Completable
            .fromCallable {
                synchronized(this.broadcasts){
                    this.broadcasts.clear()
                    this.broadcasts.addAll(broadcasts)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getBroadcast(id: Long): Single<Broadcast> {
        return Single
            .fromCallable {
                broadcasts.first { it.id == id }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getBroadcastViewersCount(broadcastId: Long): Single<Int> {
        return Single
            .fromCallable<Int> {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }
}