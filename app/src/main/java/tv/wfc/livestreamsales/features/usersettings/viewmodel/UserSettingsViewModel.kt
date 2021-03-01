package tv.wfc.livestreamsales.features.usersettings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.authorizeduser.model.user.UserInformation
import tv.wfc.livestreamsales.features.authorizeduser.repository.userinformation.IUserInformationRepository
import javax.inject.Inject

class UserSettingsViewModel @Inject constructor(
    private val userInformationRepository: IUserInformationRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IUserSettingsViewModel {
    private val disposables = CompositeDisposable()

    private var saveUserPersonalDataDisposable: Disposable? = null
    private var isUserPersonalInformationBeingSaved = BehaviorSubject.createDefault(false)
    private var isPaymentCardInformationBeingSaved = BehaviorSubject.createDefault(false)

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)

    override val name = MutableLiveData<String>()
    override val surname = MutableLiveData<String>()
    override val phoneNumber = MutableLiveData<String>()
    override val email = MutableLiveData<String>()
    override val cardNumber = MutableLiveData<String>()
//    override val cardOwnerName: LiveData<String>
//        get() = T ODO("Not yet implemented")
//    override val validThrough: LiveData<DateTime>
//        get() = T ODO("Not yet implemented")
//    override val securityCode: LiveData<String>
//        get() = T ODO("Not yet implemented")

    override val isProcessingData: LiveData<Boolean> = MutableLiveData(false).apply {
        Observable
            .combineLatest(listOf(
                isUserPersonalInformationBeingSaved,
                isPaymentCardInformationBeingSaved
            )){ states ->
                var commonState = false

                states.forEach {
                    val state = it as Boolean
                    if(state){
                        commonState = true
                        return@forEach
                    }
                }

                commonState
            }
            .distinctUntilChanged()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    init{
        prepareData()
    }

    override fun updateName(name: String) {
        if(this.name.value == name) return
        this.name.value = name
    }

    override fun updateSurname(surname: String) {
        if(this.surname.value == surname) return
        this.surname.value = surname
    }

    override fun updateEmail(email: String) {
        if(this.email.value == email) return
        this.email.value = email
    }

    @Synchronized
    override fun saveUserPersonalData() {
        if(isUserPersonalInformationBeingSaved.value == true) return

        createUserInformation()?.let{
            saveUserPersonalDataDisposable?.dispose()

            saveUserPersonalDataDisposable = userInformationRepository
                .saveUserInformation(it)
                .observeOn(mainThreadScheduler)
                .doOnSubscribe { isUserPersonalInformationBeingSaved.onNext(true) }
                .doAfterTerminate { isUserPersonalInformationBeingSaved.onNext(false) }
                .subscribeBy(onError = applicationErrorsLogger::logError)
                .addTo(disposables)
        }
    }

    @Synchronized
    override fun updateCardNumber(number: CharSequence) {
        if(cardNumber.value == number) return
        cardNumber.value = number.toString()
    }

    private fun prepareData(){
        prepareUserInformation()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .doOnError(applicationErrorsLogger::logError)
            .subscribeBy(
                onComplete = { dataPreparationState.value = ViewModelPreparationState.DataIsPrepared },
                onError = { dataPreparationState.value = ViewModelPreparationState.DataIsNotPrepared }
            )
            .addTo(disposables)
    }

    private fun prepareUserInformation(): Completable{
        return userInformationRepository
            .getUserInformation()
            .flatMapCompletable {
                Completable.merge(listOf(
                    prepareName(it),
                    prepareSurname(it),
                    preparePhoneNumber(it),
                    prepareEmail(it)
                ))
            }
    }

    private fun prepareName(userInformation: UserInformation): Completable{
        return Completable.fromRunnable {
            name.value = userInformation.name
        }
    }

    private fun prepareSurname(userInformation: UserInformation): Completable{
        return Completable.fromRunnable {
            surname.value = userInformation.surname
        }
    }

    private fun preparePhoneNumber(userInformation: UserInformation): Completable{
        return Completable.fromRunnable {
            phoneNumber.value = userInformation.phoneNumber
        }
    }

    private fun prepareEmail(userInformation: UserInformation): Completable{
        return Completable.fromRunnable {
            email.value = userInformation.email
        }
    }

    private fun createUserInformation(): UserInformation?{
        val name = this.name.value
        val surname = this.surname.value
        val phoneNumber = this.phoneNumber.value
        val email = this.email.value

        if(name.isNullOrEmpty()) return null
        if(surname.isNullOrEmpty()) return null
        if(phoneNumber.isNullOrEmpty()) return null
        if(email.isNullOrEmpty()) return null

        return UserInformation(name, surname, phoneNumber, email)
    }
}