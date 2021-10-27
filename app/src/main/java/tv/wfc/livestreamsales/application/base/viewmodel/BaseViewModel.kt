package tv.wfc.livestreamsales.application.base.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseViewModel: ViewModel() {
    private val activeOperationsCount = BehaviorSubject.createDefault(0)

    protected val disposables = CompositeDisposable()

    protected val isAnyOperationInProgressObservable: Observable<Boolean> = activeOperationsCount
        .map { it > 0 }
        .distinctUntilChanged()

    @CallSuper
    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    @Synchronized
    protected fun incrementActiveOperationsCount() {
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount + 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }

    @Synchronized
    protected fun decrementActiveOperationsCount() {
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = (currentActiveOperationsCount - 1).coerceAtLeast(0)

        activeOperationsCount.onNext(newActiveOperationsCount)
    }
}