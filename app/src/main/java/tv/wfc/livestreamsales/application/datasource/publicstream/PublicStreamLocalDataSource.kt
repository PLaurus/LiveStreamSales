package tv.wfc.livestreamsales.application.datasource.publicstream

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.stream.PublicStream
import javax.inject.Inject

class PublicStreamLocalDataSource @Inject constructor(
    @IoScheduler
    private val ioScheduler: Scheduler
): IPublicStreamDataSource {
    private val streams = mutableListOf<PublicStream>()

    override fun getAll(): Single<List<PublicStream>> {
        return Single
            .fromCallable { streams.toList() }
            .subscribeOn(ioScheduler)
    }

    override fun save(streams: List<PublicStream>): Completable {
        return Completable
            .fromCallable {
                synchronized(this.streams){
                    this.streams.clear()
                    this.streams.addAll(streams)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getById(id: Long): Single<PublicStream> {
        return Single
            .fromCallable {
                streams.first { it.id == id }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getViewersCountByStreamId(id: Long): Single<Int> {
        return Single
            .fromCallable<Int> {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getViewersCount(stream: PublicStream): Single<Int> {
        val streamId = stream.id

        return getViewersCountByStreamId(streamId)
    }
}