package tv.wfc.livestreamsales.application.repository.publicstream

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.datasource.publicstream.IPublicStreamDataSource
import tv.wfc.livestreamsales.application.di.modules.datasource.qualifiers.PublicStreamLocalDataSource
import tv.wfc.livestreamsales.application.di.modules.datasource.qualifiers.PublicStreamRemoteDataSource
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.stream.PublicStream
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class PublicStreamRepository @Inject constructor(
    @PublicStreamRemoteDataSource
    private val publicStreamRemoteDataSource: IPublicStreamDataSource,
    @PublicStreamLocalDataSource
    private val publicStreamLocalDataSource: IPublicStreamDataSource,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IPublicStreamRepository {
    private val disposables = CompositeDisposable()

    private var areBroadcastsSavedLocally = false

    override fun getAll(): Observable<List<PublicStream>> {
        return if(!areBroadcastsSavedLocally){
            getAndSaveBroadcastsInformationFromRemote().toObservable()
        } else{
            Observable.concatDelayError(listOf(
                publicStreamLocalDataSource.getAll().toObservable(),
                publicStreamRemoteDataSource.getAll().toObservable()
            ))
        }
    }

    override fun getById(id: Long): Single<PublicStream> {
        return publicStreamRemoteDataSource.getById(id)
            .onErrorResumeWith(publicStreamRemoteDataSource.getById(id))
    }

    override fun getViewersCountByStreamId(id: Long): Single<Int> {
        return publicStreamRemoteDataSource
            .getViewersCountByStreamId(id)
            .subscribeOn(ioScheduler)
    }

    override fun getViewersCount(stream: PublicStream): Single<Int> = publicStreamRemoteDataSource
        .getViewersCount(stream)
        .subscribeOn(ioScheduler)

    private fun getAndSaveBroadcastsInformationFromRemote(): Single<List<PublicStream>>{
        return publicStreamRemoteDataSource
            .getAll()
            .doOnSuccess { saveBroadcastsLocally(it) }
    }

    private fun saveBroadcastsLocally(streams: List<PublicStream>){
        publicStreamLocalDataSource
            .save(streams)
            .observeOn(mainThreadScheduler)
            .doOnComplete { areBroadcastsSavedLocally = true }
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}