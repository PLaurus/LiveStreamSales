package tv.wfc.livestreamsales.features.my_broadcasts.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laurus.p.tools.livedata.LiveEvent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.base.view_model.BaseViewModel
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.my_broadcasts.model.NextDestination
import javax.inject.Inject

class MyBroadcastsViewModel @Inject constructor(
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): BaseViewModel(), IMyBroadcastsViewModel {
    private val dataPreparationStateSubject = BehaviorSubject.createDefault<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)

    private val nextDestinationSubject = PublishSubject.create<NextDestination>()

    override val dataPreparationState: LiveData<ViewModelPreparationState> = MutableLiveData<ViewModelPreparationState>().apply {
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

    override val nextDestinationEvent = LiveEvent<NextDestination>().apply {
        nextDestinationSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun intentToCloseCurrentDestination() {
        // Todo("save data")
        nextDestinationSubject.onNext(NextDestination.Close)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}