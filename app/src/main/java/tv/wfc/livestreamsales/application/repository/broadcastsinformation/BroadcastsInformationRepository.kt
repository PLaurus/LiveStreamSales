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
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage
import javax.inject.Inject

class BroadcastsInformationRepository @Inject constructor(
    @BroadcastsInformationRemoteStorage
    private val broadcastsInformationRemoteStorage: IBroadcastsInformationStorage,
    @BroadcastsInformationLocalStorage
    private val broadcastsInformationLocalStorage: IBroadcastsInformationStorage,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IBroadcastsInformationRepository {
    private val disposables = CompositeDisposable()

    private var areBroadcastsSavedLocally = false

    override fun getBroadcasts(): Observable<List<BroadcastInformation>> {
        return if(!areBroadcastsSavedLocally){
            getAndSaveBroadcastsInformationFromRemote().toObservable()
        } else{
            Observable.concatDelayError(listOf(
                broadcastsInformationLocalStorage.getBroadcasts().toObservable(),
                broadcastsInformationRemoteStorage.getBroadcasts().toObservable()
            ))
        }
    }

    override fun getBroadcast(id: Long): Single<BroadcastInformation> {
        return broadcastsInformationRemoteStorage.getBroadcast(id)
            .onErrorResumeWith(broadcastsInformationRemoteStorage.getBroadcast(id))
    }

    private fun getAndSaveBroadcastsInformationFromRemote(): Single<List<BroadcastInformation>>{
        return broadcastsInformationRemoteStorage
            .getBroadcasts()
            .doOnSuccess { saveBroadcastsLocally(it) }
    }

    private fun saveBroadcastsLocally(broadcasts: List<BroadcastInformation>){
        broadcastsInformationLocalStorage
            .saveBroadcasts(broadcasts)
            .observeOn(mainThreadScheduler)
            .doOnComplete { areBroadcastsSavedLocally = true }
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}