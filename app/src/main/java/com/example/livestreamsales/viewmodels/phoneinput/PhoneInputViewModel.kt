package com.example.livestreamsales.viewmodels.phoneinput

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import com.example.livestreamsales.repository.authorization.IAuthorizationRepository
import com.example.livestreamsales.utils.LiveEvent
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class PhoneInputViewModel @Inject constructor(
    private val authorizationRepository: IAuthorizationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler
): ViewModel(), IPhoneInputViewModel {
    private val disposables = CompositeDisposable()

    private val phoneNumberSubject = BehaviorSubject.createDefault("")
    private val isVerificationCodeSentSubject = PublishSubject.create<Boolean>()
    private val isTelephoneNumberCorrectSubject = PublishSubject.create<Boolean>()
    private val canRequestCodeSubject = Observable
        .combineLatest(
            isTelephoneNumberCorrectSubject,
            authorizationRepository.isCodeRequestAvailable,
            { isTelephoneNumberCorrect, isCodeRequestAvailable ->
                isTelephoneNumberCorrect && isCodeRequestAvailable
            }
        )

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)

    override val phoneNumber: LiveData<String> = LiveDataReactiveStreams.fromPublisher(
        phoneNumberSubject.toFlowable(BackpressureStrategy.LATEST)
    )

    override val newCodeRequestWaitingTime: LiveData<Long> = MutableLiveData(0L).apply{
        authorizationRepository.nextCodeRequestWaitingTime
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val isCodeRequestAvailable: LiveData<Boolean> = MutableLiveData(false).apply{
        authorizationRepository.isCodeRequestAvailable
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val isVerificationCodeSent: LiveData<Boolean> = LiveEvent<Boolean>().apply{
        isVerificationCodeSentSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val isTelephoneNumberCorrect: LiveData<Boolean> = MutableLiveData(false).apply {
        isTelephoneNumberCorrectSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val canUserRequestCode: LiveData<Boolean> = MutableLiveData(false).apply{
        canRequestCodeSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    init{
        observeTelephoneNumber()
    }

    override fun updatePhoneNumber(phoneNumber: String){
        phoneNumberSubject.onNext(phoneNumber)
    }

    override fun requestVerificationCode(){
        authorizationRepository.sendVerificationCodeRequest(phoneNumberSubject.value)
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