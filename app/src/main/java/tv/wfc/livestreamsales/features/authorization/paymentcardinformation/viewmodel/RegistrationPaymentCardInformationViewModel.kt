package tv.wfc.livestreamsales.features.authorization.paymentcardinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.IPaymentCardInformationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class RegistrationPaymentCardInformationViewModel @Inject constructor(
    private val paymentCardInformationRepository: IPaymentCardInformationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IRegistrationPaymentCardInformationViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)

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

    override fun updatePaymentCardInformation(token: String) {
        paymentCardInformationRepository
            .updatePaymentCardInformation(token)
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
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