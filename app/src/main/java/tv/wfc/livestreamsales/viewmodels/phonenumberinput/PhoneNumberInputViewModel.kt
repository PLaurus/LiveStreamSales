package tv.wfc.livestreamsales.viewmodels.phonenumberinput

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import tv.wfc.livestreamsales.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.utils.LiveEvent
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class PhoneNumberInputViewModel @Inject constructor(
    private val authorizationRepository: IAuthorizationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler
): ViewModel(), IPhoneNumberInputViewModel {
    private val disposables = CompositeDisposable()

    private val phoneNumberSubject = BehaviorSubject.createDefault("")
    private val isConfirmationCodeSentSubject = PublishSubject.create<Boolean>()
    private val isPhoneNumberCorrectSubject = PublishSubject.create<Boolean>()
    private val canRequestCodeSubject = Observable
        .combineLatest(
            isPhoneNumberCorrectSubject,
            authorizationRepository.isCodeRequestAvailable,
            { isPhoneNumberCorrect, isCodeRequestAvailable ->
                isPhoneNumberCorrect && isCodeRequestAvailable
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

    override val isConfirmationCodeSent: LiveData<Boolean> = LiveEvent<Boolean>().apply{
        isConfirmationCodeSentSubject
            .observeOn(mainThreadScheduler)
            .subscribe(::setValue)
            .addTo(disposables)
    }

    override val isPhoneNumberCorrect: LiveData<Boolean> = MutableLiveData(false).apply {
        isPhoneNumberCorrectSubject
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
        observePhoneNumber()
    }

    override fun updatePhoneNumber(phoneNumber: String){
        phoneNumberSubject.onNext(phoneNumber)
    }

    override fun requestConfirmationCode(){
        authorizationRepository.sendConfirmationCodeRequest(phoneNumberSubject.value)
            .subscribeOn(mainThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(isConfirmationCodeSentSubject::onNext)
            .addTo(disposables)
    }

    private fun observePhoneNumber(){
        phoneNumberSubject
            .subscribeOn(mainThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe(::checkPhoneNumberCorrectness)
            .addTo(disposables)
    }

    private fun checkPhoneNumberCorrectness(phoneNumber: String){
        val isCorrect = !(phoneNumber.isEmpty() || phoneNumber.isBlank())

        isPhoneNumberCorrectSubject.onNext(isCorrect)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}