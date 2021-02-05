package tv.wfc.livestreamsales.features.mainappcontent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class MainAppContentViewModel @Inject constructor(
    authorizationRepository: IAuthorizationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler
): ViewModel(), IMainAppContentViewModel {
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