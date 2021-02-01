package tv.wfc.livestreamsales.viewmodels.phonenumberconfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.application.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.model.application.phonenumberconfirmation.PhoneNumberConfirmationResult
import tv.wfc.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import tv.wfc.livestreamsales.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.utils.LiveEvent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class PhoneNumberConfirmationViewModel @Inject constructor(
    private val authorizationRepository: IAuthorizationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IPhoneNumberConfirmationViewModel {
    companion object{
        private const val TERMS_OF_THE_OFFER_URL = "http://www.google.com/"
    }

    private val disposables = CompositeDisposable()

    private val dataPreparationStateSubject = PublishSubject.create<ViewModelPreparationState>()

    private val phoneNumberSubject = BehaviorSubject.createDefault("")
    private val codeSubject = BehaviorSubject.createDefault("")
    private val isCodeBeingCheckedSubject = PublishSubject.create<Boolean>()
    private val phoneConfirmationResultSubject = PublishSubject.create<PhoneNumberConfirmationResult>()
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

    override val phoneNumberConfirmationResult: LiveData<PhoneNumberConfirmationResult> =
        LiveEvent<PhoneNumberConfirmationResult>().apply {
            phoneConfirmationResultSubject
                .observeOn(mainThreadScheduler)
                .subscribe(::setValue)
                .addTo(disposables)
        }

    override val phoneNumberConfirmationErrors: LiveData<String> = LiveEvent<String>().apply{
        phoneConfirmationErrorsSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
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

    override fun confirmPhoneNumber() {
        codeSubject
            .firstOrError()
            .map{ it.toInt() }
            .flatMap{ intCode ->
                authorizationRepository.confirmPhoneNumber(phoneNumberSubject.value, intCode)
            }
            .doOnSubscribe { isCodeBeingCheckedSubject.onNext(true) }
            .doAfterSuccess { isCodeBeingCheckedSubject.onNext(false) }
            .doOnError{ isCodeBeingCheckedSubject.onNext(false) }
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = { confirmationResult ->
                    phoneConfirmationResultSubject.onNext(confirmationResult)

                    if(confirmationResult is PhoneNumberConfirmationResult.PhoneNumberIsNotConfirmed){
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
        authorizationRepository.sendConfirmationCodeRequest(phoneNumberSubject.value)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}