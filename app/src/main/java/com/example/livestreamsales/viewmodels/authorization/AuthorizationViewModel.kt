package com.example.livestreamsales.viewmodels.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.model.network.rest.error.ResponseError
import com.example.livestreamsales.network.rest.errors.IResponseErrorsManager
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(
    private val responseErrorsManager: IResponseErrorsManager,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
): ViewModel(), IAuthorizationViewModel {
    private val disposables = CompositeDisposable()

    private val phoneNumberSubject = PublishSubject.create<String>()

    override val responseError: LiveData<ResponseError?> = MutableLiveData<ResponseError?>().apply{
        responseErrorsManager.errors
            .observeOn(mainThreadScheduler)
            .subscribe{
                value = it
                value = null
            }
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