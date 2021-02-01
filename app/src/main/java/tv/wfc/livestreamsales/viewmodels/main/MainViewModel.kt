package tv.wfc.livestreamsales.viewmodels.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.repository.authorization.IAuthorizationRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class MainViewModel @Inject constructor(
    authorizationRepository: IAuthorizationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler
): ViewModel(), IMainViewModel {
    private val disposables = CompositeDisposable()

    override val isUserLoggedIn: LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        authorizationRepository.isUserLoggedIn
            .subscribeOn(mainThreadScheduler)
            .subscribe(this::setValue)
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}