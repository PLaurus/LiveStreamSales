package tv.wfc.livestreamsales.features.mystreams.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laurus.p.tools.livedata.LiveEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.base.viewmodel.BaseViewModel
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.application.repository.mystream.IMyStreamRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.mystreams.model.NextDestination
import javax.inject.Inject

class MyStreamsViewModel @Inject constructor(
    private val myStreamRepository: IMyStreamRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
) : BaseViewModel(), IMyStreamsViewModel {
    private val dataPreparationStateSubject =
        BehaviorSubject.createDefault<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)

    private val isDataBeingRefreshedSubject = BehaviorSubject.createDefault(false)

    private val nextDestinationSubject = PublishSubject.create<NextDestination>()

    private val myStreamsSubject = BehaviorSubject.create<List<MyStream>>()

    private var dataPreparationDisposable: Disposable? = null

    private var dataRefreshmentDisposable: Disposable? = null

    init {
        prepareData()
    }

    override val dataPreparationState: LiveData<ViewModelPreparationState> =
        MutableLiveData<ViewModelPreparationState>().apply {
            dataPreparationStateSubject
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = ::setValue,
                    onError = applicationErrorsLogger::logError
                )
                .addTo(disposables)
        }

    override val isAnyOperationInProgress: LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        isAnyOperationInProgressObservable
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isDataBeingRefreshed: LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        isDataBeingRefreshedSubject
            .distinctUntilChanged()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val nextDestinationEvent = LiveEvent<NextDestination>().apply {
        nextDestinationSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val myStreams: LiveData<List<MyStream>> = MutableLiveData<List<MyStream>>().apply {
        myStreamsSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun refreshData() {
        dataRefreshmentDisposable?.dispose()

        dataRefreshmentDisposable = updateWithStorageData()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe {
                dataPreparationStateSubject.onNext(ViewModelPreparationState.DataIsBeingPrepared)
                isDataBeingRefreshedSubject.onNext(true)
            }
            .doOnTerminate { isDataBeingRefreshedSubject.onNext(false) }
            .subscribeBy(
                onComplete = { dataPreparationStateSubject.onNext(ViewModelPreparationState.DataIsPrepared) },
                onError = {
                    dataPreparationStateSubject.onNext(ViewModelPreparationState.FailedToPrepareData(it.localizedMessage))
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    override fun prepareToCloseCurrentDestination() {
        nextDestinationSubject.onNext(NextDestination.Close)
    }

    override fun prepareToNavigateToStreamCreationDestination() {
        nextDestinationSubject.onNext(NextDestination.StreamCreation)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private fun prepareData() {
        if(dataPreparationStateSubject.value == ViewModelPreparationState.DataIsPrepared) return

        dataPreparationDisposable?.dispose()

        dataPreparationDisposable = updateWithStorageData()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationStateSubject.onNext(ViewModelPreparationState.DataIsBeingPrepared) }
            .subscribeBy(
                onComplete = { dataPreparationStateSubject.onNext(ViewModelPreparationState.DataIsPrepared) },
                onError = {
                    dataPreparationStateSubject.onNext(ViewModelPreparationState.FailedToPrepareData(it.localizedMessage))
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    private fun updateWithStorageData(): Completable =
         Completable
             .mergeArray(
                 prepareMyStreams()
             )
             .subscribeOn(mainThreadScheduler)

    private fun prepareMyStreams(): Completable = myStreamRepository
        .getMyStreams()
        .flatMapCompletable { myStreams ->
            Completable.fromRunnable {
                myStreamsSubject.onNext(myStreams)
            }
        }
        .subscribeOn(mainThreadScheduler)
}