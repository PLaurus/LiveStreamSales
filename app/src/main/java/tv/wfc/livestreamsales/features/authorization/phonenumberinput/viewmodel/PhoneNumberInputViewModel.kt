package tv.wfc.livestreamsales.features.authorization.phonenumberinput.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.tools.livedata.LiveEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.authorization.repository.ILoginRepository
import javax.inject.Inject

class PhoneNumberInputViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    private val loginRepository: ILoginRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IPhoneNumberInputViewModel {
    private val disposables = CompositeDisposable()

    private val phoneNumberSubject = BehaviorSubject.createDefault("")
    private val isConfirmationCodeSentSubject = PublishSubject.create<Boolean>()
    private val isPhoneNumberCorrectSubject = PublishSubject.create<Boolean>()
    private val canRequestCodeSubject = Observable
        .combineLatest(
            isPhoneNumberCorrectSubject,
            authorizationManager.isCodeRequestAvailable,
            { isPhoneNumberCorrect, isCodeRequestAvailable ->
                isPhoneNumberCorrect && isCodeRequestAvailable
            }
        )

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)

    override val phoneNumber: LiveData<String> = MutableLiveData<String>().apply {
        phoneNumberSubject
            .observeOn(mainThreadScheduler)
            .doOnNext(::savePhoneNumberToRepository)
            .subscribeBy(
                onNext = { it.removePrefix("+7") },
                onError = applicationErrorsLogger::logError
            )
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
        phoneNumberSubject.onNext("+7$phoneNumber")
    }

    private fun savePhoneNumberToRepository(phoneNumber: String){
        loginRepository
            .saveLogin(phoneNumber)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun requestConfirmationCode(){
        authorizationManager.requestConfirmationCode(phoneNumberSubject.value)
            .subscribeOn(ioScheduler)
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