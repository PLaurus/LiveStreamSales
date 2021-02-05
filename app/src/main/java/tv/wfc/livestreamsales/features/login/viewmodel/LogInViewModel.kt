package tv.wfc.livestreamsales.features.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.features.rest.model.ResponseError
import tv.wfc.livestreamsales.features.rest.errors.IResponseErrorsManager
import tv.wfc.livestreamsales.application.tools.livedata.LiveEvent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    private val responseErrorsManager: IResponseErrorsManager,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
): ViewModel(), ILogInViewModel {
    private val disposables = CompositeDisposable()

    private val phoneNumberSubject = PublishSubject.create<String>()

    override val responseError: LiveData<ResponseError> = LiveEvent<ResponseError>().apply{
        responseErrorsManager.errors
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val phoneNumber: LiveData<String> = MutableLiveData("").apply{
        phoneNumberSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override fun updatePhoneNumber(phoneNumber: String){
        phoneNumberSubject.onNext(phoneNumber)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}