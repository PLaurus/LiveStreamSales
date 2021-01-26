package com.example.livestreamsales.viewmodels.phoneconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.application.errors.IApplicationErrorsLogger
import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class PhoneConfirmationViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IPhoneConfirmationViewModel {
    companion object{
        private const val TERMS_OF_THE_OFFER_URL = "http://www.google.com/"
    }

    private val disposables = CompositeDisposable()

    private val dataPreparationStateSubject = PublishSubject.create<ViewModelPreparationState>()

    private val phoneNumberSubject = BehaviorSubject.createDefault("")
    private val codeSubject = BehaviorSubject.createDefault(0)
    private val isCodeBeingCheckedSubject = PublishSubject.create<Boolean>()
    private val phoneConfirmationResultSubject = PublishSubject.create<IPhoneConfirmationViewModel.PhoneConfirmationResult>()
    private val codeVerificationErrorsSubject = PublishSubject.create<String>()

    override val dataPreparationState: LiveData<ViewModelPreparationState> =
        MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared).apply {
            dataPreparationStateSubject
                .distinctUntilChanged()
                .observeOn(mainThreadScheduler)
                .subscribe(::setValue)
                .addTo(disposables)
        }

    override val phoneNumber: LiveData<String> = MutableLiveData("").apply {
        phoneNumberSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val code: LiveData<Int> = MutableLiveData(0).apply {
        codeSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val codeLength: LiveData<Int> = MutableLiveData(0).apply{
        authorizationManager.getVerificationCodeLength()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe {
                dataPreparationStateSubject.onNext(ViewModelPreparationState.DataIsBeingPrepared)
            }
            .doAfterSuccess {
                dataPreparationStateSubject.onNext(ViewModelPreparationState.DataIsPrepared)
            }
            .doOnError {
                dataPreparationStateSubject.onNext(ViewModelPreparationState.FailedToPrepareData())
            }
            .subscribeBy(
                onSuccess = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isCodeBeingChecked: LiveData<Boolean> = MutableLiveData(false).apply{
        isCodeBeingCheckedSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val phoneConfirmationResult: LiveData<IPhoneConfirmationViewModel.PhoneConfirmationResult?> =
        MutableLiveData<IPhoneConfirmationViewModel.PhoneConfirmationResult?>().apply {
            phoneConfirmationResultSubject
                .observeOn(mainThreadScheduler)
                .subscribe{ result ->
                    value = result
                    value = null
                }
                .addTo(disposables)
        }

    override val phoneConfirmationErrors: LiveData<String?> = MutableLiveData<String?>().apply{
        codeVerificationErrorsSubject
            .observeOn(mainThreadScheduler)
            .subscribe{
                value = it
                value = null
            }
            .addTo(disposables)
    }

    override val newCodeRequestWaitingTime: LiveData<Long> = MutableLiveData(0L).apply{
        authorizationManager.nextCodeRequestWaitingTime
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val isCodeRequestAvailable: LiveData<Boolean> = MutableLiveData(false).apply{
        authorizationManager.isCodeRequestAvailable
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val termsOfTheOfferUrl: LiveData<String> = MutableLiveData(TERMS_OF_THE_OFFER_URL)

    override fun updatePhoneNumber(phoneNumber: String) {
        phoneNumberSubject.onNext(phoneNumber)
    }

    override fun updateCode(code: String){
        val intCode = try{
            code.toInt()
        } catch(ex: NumberFormatException) {
            0
        }

        codeSubject.onNext(intCode)
    }

    override fun confirmPhoneNumber() {
        authorizationManager.logIn(phoneNumberSubject.value, codeSubject.value)
            .doOnSubscribe { isCodeBeingCheckedSubject.onNext(true) }
            .doAfterSuccess { isCodeBeingCheckedSubject.onNext(false) }
            .doOnError{ isCodeBeingCheckedSubject.onNext(false) }
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = { logInResult ->
                    logInResult.errorMessage?.let{
                        codeVerificationErrorsSubject.onNext(it)
                    }

                    if(logInResult.isLoggedIn){
                        if(logInResult.needRegistration){
                            phoneConfirmationResultSubject.onNext(
                                IPhoneConfirmationViewModel.PhoneConfirmationResult.PHONE_CONFIRMED_NEED_REGISTRATION
                            )
                        } else{
                            phoneConfirmationResultSubject.onNext(
                                IPhoneConfirmationViewModel.PhoneConfirmationResult.PHONE_CONFIRMED
                            )
                        }
                    } else{
                        phoneConfirmationResultSubject.onNext(
                            IPhoneConfirmationViewModel.PhoneConfirmationResult.PHONE_NOT_CONFIRMED
                        )
                    }
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun requestNewCode() {
        authorizationManager.sendVerificationCodeRequest(phoneNumberSubject.value)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}