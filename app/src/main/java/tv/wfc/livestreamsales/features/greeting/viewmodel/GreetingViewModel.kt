package tv.wfc.livestreamsales.features.greeting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.repository.applicationsettings.IApplicationSettingsRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage
import tv.wfc.livestreamsales.features.greeting.repository.IGreetingPageRepository
import javax.inject.Inject

class GreetingViewModel @Inject constructor(
    private val applicationSettingsRepository: IApplicationSettingsRepository,
    private val greetingPageRepository: IGreetingPageRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IGreetingViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)

    override val greetingPages = MutableLiveData<Set<GreetingPage>>().apply {
        greetingPageRepository
            .getGreetingPages()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isAnyOperationInProgress = MutableLiveData<Boolean>().apply {
        activeOperationsCount
            .observeOn(mainThreadScheduler)
            .map { it > 0 }
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isShownStateSaved = MutableLiveData<Boolean>()

    private var notifyGreetingIsShownDisposable: Disposable? = null

    override fun saveGreetingIsShown() {
        notifyGreetingIsShownDisposable?.dispose()
        notifyGreetingIsShownDisposable = applicationSettingsRepository.saveIsGreetingShown(true)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate { decrementActiveOperationsCount() }
            .subscribeBy(
                onComplete = { isShownStateSaved.value = true },
                onError = {
                    isShownStateSaved.value = false
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    @Synchronized
    private fun incrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount + 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }

    @Synchronized
    private fun decrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount - 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }
}