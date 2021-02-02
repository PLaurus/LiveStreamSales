package tv.wfc.livestreamsales.repository.broadcastsinformation

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.storage.qualifiers.BroadcastsInformationLocalStorage
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.storage.qualifiers.BroadcastsInformationRemoteStorage
import tv.wfc.livestreamsales.model.application.broadcastinformation.BroadcastBaseInformation
import tv.wfc.livestreamsales.storage.broadcastsinformation.IBroadcastsInformationStorage
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

    override fun getBroadcasts(): Observable<List<BroadcastBaseInformation>> {
        return if(!areBroadcastsSavedLocally){
            getAndSaveBroadcastsInformationFromRemote().toObservable()
        } else{
            Observable.concatDelayError(listOf(
                broadcastsInformationLocalStorage.getBroadcasts().toObservable(),
                broadcastsInformationRemoteStorage.getBroadcasts().toObservable()
            ))
        }
    }

    private fun getAndSaveBroadcastsInformationFromRemote(): Single<List<BroadcastBaseInformation>>{
        return broadcastsInformationRemoteStorage
            .getBroadcasts()
            .doOnSuccess { saveBroadcastsLocally(it) }
    }

    private fun saveBroadcastsLocally(broadcasts: List<BroadcastBaseInformation>){
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