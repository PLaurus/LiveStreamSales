package tv.wfc.livestreamsales.application.repository.broadcastsinformation

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.di.modules.datastore.qualifiers.BroadcastsLocalDataStore
import tv.wfc.livestreamsales.application.di.modules.datastore.qualifiers.BroadcastsRemoteDataStore
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsDataStore
import javax.inject.Inject

class BroadcastsRepository @Inject constructor(
    @BroadcastsRemoteDataStore
    private val broadcastsRemoteDataStore: IBroadcastsDataStore,
    @BroadcastsLocalDataStore
    private val broadcastsLocalDataStore: IBroadcastsDataStore,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IBroadcastsRepository {
    private val disposables = CompositeDisposable()

    private var areBroadcastsSavedLocally = false

    override fun getBroadcasts(): Observable<List<Broadcast>> {
        return if(!areBroadcastsSavedLocally){
            getAndSaveBroadcastsInformationFromRemote().toObservable()
        } else{
            Observable.concatDelayError(listOf(
                broadcastsLocalDataStore.getBroadcasts().toObservable(),
                broadcastsRemoteDataStore.getBroadcasts().toObservable()
            ))
        }
    }

    override fun getBroadcast(id: Long): Single<Broadcast> {
        return broadcastsRemoteDataStore.getBroadcast(id)
            .onErrorResumeWith(broadcastsRemoteDataStore.getBroadcast(id))
    }

    override fun getBroadcastViewersCount(broadcastId: Long): Single<Int> {
        return broadcastsRemoteDataStore
            .getBroadcastViewersCount(broadcastId)
            .subscribeOn(ioScheduler)
    }

    private fun getAndSaveBroadcastsInformationFromRemote(): Single<List<Broadcast>>{
        return broadcastsRemoteDataStore
            .getBroadcasts()
            .doOnSuccess { saveBroadcastsLocally(it) }
    }

    private fun saveBroadcastsLocally(broadcasts: List<Broadcast>){
        broadcastsLocalDataStore
            .saveBroadcasts(broadcasts)
            .observeOn(mainThreadScheduler)
            .doOnComplete { areBroadcastsSavedLocally = true }
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}