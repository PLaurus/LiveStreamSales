package tv.wfc.livestreamsales.application.repository.broadcastsinformation

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.BroadcastsInformationLocalStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.BroadcastsInformationRemoteStorage
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsStorage
import javax.inject.Inject

class BroadcastsInformationRepository @Inject constructor(
    @BroadcastsInformationRemoteStorage
    private val broadcastsRemoteStorage: IBroadcastsStorage,
    @BroadcastsInformationLocalStorage
    private val broadcastsLocalStorage: IBroadcastsStorage,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IBroadcastsInformationRepository {
    private val disposables = CompositeDisposable()

    private var areBroadcastsSavedLocally = false

    override fun getBroadcasts(): Observable<List<Broadcast>> {
        return if(!areBroadcastsSavedLocally){
            getAndSaveBroadcastsInformationFromRemote().toObservable()
        } else{
            Observable.concatDelayError(listOf(
                broadcastsLocalStorage.getBroadcasts().toObservable(),
                broadcastsRemoteStorage.getBroadcasts().toObservable()
            ))
        }
    }

    override fun getBroadcast(id: Long): Single<Broadcast> {
        return broadcastsRemoteStorage.getBroadcast(id)
            .onErrorResumeWith(broadcastsRemoteStorage.getBroadcast(id))
    }

    private fun getAndSaveBroadcastsInformationFromRemote(): Single<List<Broadcast>>{
        return broadcastsRemoteStorage
            .getBroadcasts()
            .doOnSuccess { saveBroadcastsLocally(it) }
    }

    private fun saveBroadcastsLocally(broadcasts: List<Broadcast>){
        broadcastsLocalStorage
            .saveBroadcasts(broadcasts)
            .observeOn(mainThreadScheduler)
            .doOnComplete { areBroadcastsSavedLocally = true }
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}