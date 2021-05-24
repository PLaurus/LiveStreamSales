package tv.wfc.livestreamsales.features.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IHomeViewModel {
    private val disposables = CompositeDisposable()

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared) // TODO: implement it

    override val isUserAuthorized: LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        authorizationManager.isUserLoggedIn
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isCardLinkedToAccount: LiveData<Boolean> = MutableLiveData(false) // TODO: implement it

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}