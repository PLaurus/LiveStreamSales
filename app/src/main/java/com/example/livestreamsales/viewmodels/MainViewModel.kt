package com.example.livestreamsales.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.authorization.IAuthorizationManager
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class MainViewModel(
    authorizationManager: IAuthorizationManager,
    mainThreadScheduler: Scheduler
): ViewModel(){
    private val disposables = CompositeDisposable()
    private val isUserLoggedInMutableLiveData = MutableLiveData<Boolean>(false)

    val isUserLoggedIn: LiveData<Boolean> = isUserLoggedInMutableLiveData.apply{
        authorizationManager.isUserLoggedIn
            .observeOn(mainThreadScheduler)
            .subscribe(this::setValue)
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}