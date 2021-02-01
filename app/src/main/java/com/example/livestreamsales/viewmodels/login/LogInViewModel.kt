package com.example.livestreamsales.viewmodels.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.model.network.rest.error.ResponseError
import com.example.livestreamsales.network.rest.errors.IResponseErrorsManager
import com.example.livestreamsales.utils.LiveEvent
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