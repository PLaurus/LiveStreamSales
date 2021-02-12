package tv.wfc.livestreamsales.features.mainpage.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation
import tv.wfc.livestreamsales.application.model.viewmodel.ViewModelPreparationState
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.IBroadcastsInformationRepository
import javax.inject.Inject

class MainPageViewModel @Inject constructor(
    private val broadcastsInformationRepository: IBroadcastsInformationRepository,
    private val applicationErrorsLogger: IApplicationErrorsLogger,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler
): ViewModel(), IMainPageViewModel {
    private val disposables = CompositeDisposable()

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>()

    override val isDataBeingRefreshed = MutableLiveData(false)

    override val liveBroadcasts = MutableLiveData<List<BroadcastInformation>>()

    override val announcements = MutableLiveData<List<BroadcastInformation>>()

    init{
        prepareData()
    }

    override fun refreshData() {
        if(isDataBeingRefreshed.value == true)
            return

        refreshBroadcasts()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { isDataBeingRefreshed.value = true }
            .doOnTerminate { isDataBeingRefreshed.value = false }
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
    }

    override fun getLiveBroadcastTitleByPosition(position: Int): String {
        return liveBroadcasts.value
            ?.getOrNull(position)
            ?.title ?: ""
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    private fun prepareData() {
        if(dataPreparationState.value == ViewModelPreparationState.DataIsBeingPrepared)
            return

        prepareBroadcasts()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .subscribeBy(
                onComplete = {
                    dataPreparationState.value = ViewModelPreparationState.DataIsPrepared
                },
                onError = {
                    applicationErrorsLogger.logError(it)
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData()
                }
            )
            .addTo(disposables)
    }

    private fun prepareBroadcasts() = Completable.create{ emitter ->
        val disposable = getBroadcastsFromRepository(
            onNext = { emitter.onComplete() },
            onComplete = { emitter.onComplete() },
            onError = emitter::onError
        )

        emitter.setDisposable(disposable)
    }

    private fun refreshBroadcasts() = Completable.create{ emitter ->
        val disposable = getBroadcastsFromRepository(
            onComplete = { emitter.onComplete() },
            onError = emitter::onError
        )

        emitter.setDisposable(disposable)
    }

    private fun getBroadcastsFromRepository(
        onComplete: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onNext: ((List<BroadcastInformation>) -> Unit)? = null
    ): Disposable{
        return broadcastsInformationRepository
            .getBroadcasts()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = {
                    updateBroadcastsInformation(it)
                    onNext?.invoke(it)
                },
                onComplete = {
                    onComplete?.invoke()
                },
                onError = {
                    onError?.invoke(it)
                }
            )
    }

    private fun updateBroadcastsInformation(broadcasts: List<BroadcastInformation>){
        val liveBroadcasts = broadcasts.filter{ broadcastInformation ->
            broadcastInformation.startsAt?.isBeforeNow == true
        }

        val broadcastAnnouncements = broadcasts.filter{ broadcastInformation ->
            broadcastInformation.startsAt?.isAfterNow == true
        }

        this.liveBroadcasts.value = liveBroadcasts
        this.announcements.value = broadcastAnnouncements
    }
}