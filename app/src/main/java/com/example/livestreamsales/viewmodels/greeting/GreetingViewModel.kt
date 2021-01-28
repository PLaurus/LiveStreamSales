package com.example.livestreamsales.viewmodels.greeting

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class GreetingViewModel @Inject constructor(

): ViewModel(), IGreetingViewModel {
    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}