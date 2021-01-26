package com.example.livestreamsales.viewmodels.phoneconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livestreamsales.application.errors.IApplicationErrorsLogger
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.model.application.phoneconfirmation.PhoneConfirmationResult
import com.example.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import com.example.livestreamsales.repository.authorization.IAuthorizationRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class PhoneConfirmationViewModel @Inject constructor(
    private val authorizationRepository: IAuthorizationRepository,
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
    private val codeSubject = BehaviorSubject.createDefault("")
    private val isCodeBeingCheckedSubject = PublishSubject.create<Boolean>()
    private val phoneConfirmationResultSubject = PublishSubject.create<PhoneConfirmationResult>()
    private val phoneConfirmationErrorsSubject = PublishSubject.create<String>()

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

    override val code: LiveData<String> = MutableLiveData("").apply {
        codeSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val codeLength: LiveData<Int> = MutableLiveData(0).apply{
        authorizationRepository.getRequiredCodeLength()
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

    override val phoneConfirmationResult: LiveData<PhoneConfirmationResult?> =
        MutableLiveData<PhoneConfirmationResult?>().apply {
            phoneConfirmationResultSubject
                .observeOn(mainThreadScheduler)
                .subscribe{ result ->
                    value = result
                    value = null
                }
                .addTo(disposables)
        }

    override val phoneConfirmationErrors: LiveData<String?> = MutableLiveData<String?>().apply{
        phoneConfirmationErrorsSubject
            .observeOn(mainThreadScheduler)
            .subscribe{
                value = it
                value = null
            }
            .addTo(disposables)
    }

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

    override val termsOfTheOfferUrl: LiveData<String> = MutableLiveData(TERMS_OF_THE_OFFER_URL)

    override fun updatePhoneNumber(phoneNumber: String) {
        phoneNumberSubject.onNext(phoneNumber)
    }

    override fun updateCode(code: String){
        codeSubject.onNext(code)
    }

    override fun confirmPhone() {
        codeSubject
            .firstOrError()
            .map{ it.toInt() }
            .flatMap{ intCode ->
                authorizationRepository.confirmPhone(phoneNumberSubject.value, intCode)
            }
            .doOnSubscribe { isCodeBeingCheckedSubject.onNext(true) }
            .doAfterSuccess { isCodeBeingCheckedSubject.onNext(false) }
            .doOnError{ isCodeBeingCheckedSubject.onNext(false) }
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = { confirmationResult ->
                    phoneConfirmationResultSubject.onNext(confirmationResult)

                    if(confirmationResult is PhoneConfirmationResult.PhoneIsNotConfirmed){
                        confirmationResult.errorMessage?.let{
                            phoneConfirmationErrorsSubject.onNext(it)
                        }
                    }
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun requestNewCode() {
        authorizationRepository.sendVerificationCodeRequest(phoneNumberSubject.value)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}