package tv.wfc.livestreamsales.features.mainappcontent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import javax.inject.Inject

class MainAppContentViewModel @Inject constructor(
    authorizationManager: IAuthorizationManager,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler
): ViewModel(), IMainAppContentViewModel {
    private val disposables = CompositeDisposable()

    override val isUserLoggedIn: LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        authorizationManager.isUserLoggedIn
            .subscribeOn(mainThreadScheduler)
            .subscribe(this::setValue)
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}