package com.example.livestreamsales.viewmodels.telephonenumberinput

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.di.components.app.ReactiveXModule
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

class TelephoneNumberInputViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    @Named(ReactiveXModule.DEPENDENCY_NAME_MAIN_THREAD_SCHEDULER)
    private val mainThreadScheduler: Scheduler,
    @Named(ReactiveXModule.DEPENDENCY_NAME_IO_SCHEDULER)
    private val ioScheduler: Scheduler
): ViewModel(), ITelephoneNumberInputViewModel {
    private val disposables = CompositeDisposable()

    private val phoneNumberSubject = BehaviorSubject.createDefault("")
    private val isVerificationCodeSentSubject = PublishSubject.create<Boolean>()
    private val isTelephoneNumberCorrectSubject = PublishSubject.create<Boolean>()

    override val phoneNumber: LiveData<String> = LiveDataReactiveStreams.fromPublisher(
        phoneNumberSubject.toFlowable(BackpressureStrategy.LATEST)
    )

    override val isVerificationCodeSent: LiveData<Boolean> = LiveDataReactiveStreams.fromPublisher(
        isVerificationCodeSentSubject.toFlowable(BackpressureStrategy.LATEST)
    )

    override val isTelephoneNumberCorrect: LiveData<Boolean> = LiveDataReactiveStreams.fromPublisher(
        isTelephoneNumberCorrectSubject.toFlowable(BackpressureStrategy.LATEST)
    )

    init{
        observeTelephoneNumber()
    }

    override fun updatePhoneNumber(phoneNumber: String){
        phoneNumberSubject.onNext(phoneNumber)
    }

    override fun requestVerificationCode(){
        authorizationManager.sendVerificationCodeRequest(phoneNumberSubject.value)
            .subscribeOn(mainThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(isVerificationCodeSentSubject::onNext)
            .addTo(disposables)
    }

    private fun observeTelephoneNumber(){
        phoneNumberSubject
            .subscribeOn(mainThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(::checkTelephoneNumberCorrectness)
            .addTo(disposables)
    }

    private fun checkTelephoneNumberCorrectness(phoneNumber: String){
        val isCorrect = !(phoneNumber.isEmpty() || phoneNumber.isBlank())

        isTelephoneNumberCorrectSubject.onNext(isCorrect)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}