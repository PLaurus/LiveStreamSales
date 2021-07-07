package tv.wfc.livestreamsales.features.authorization.userpersonalinformation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.tools.livedata.LiveEvent
import com.laurus.p.tools.string.isNotEmail
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.model.userpersonalinformation.UserPersonalInformation
import tv.wfc.livestreamsales.application.repository.userpersonalinformation.IUserPersonalInformationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import java.util.regex.Pattern
import javax.inject.Inject

class RegistrationUserPersonalInformationViewModel @Inject constructor(
    private val userPersonalInformationRepository: IUserPersonalInformationRepository,
    private val authorizationManager: IAuthorizationManager,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IRegistrationUserPersonalInformationViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)

    private var isRegistrationFlow: Boolean = false
    private var authorizationToken: String? = null
    private var dataPreparationDisposable: Disposable? = null
    private var logOutDisposable: Disposable? = null
    private var saveUserPersonalDataDisposable: Disposable? = null
    private var minNameLength: Int? = null
    private var maxNameLength: Int? = null
    private var minSurnameLength: Int? = null
    private var maxSurnameLength: Int? = null
    private var minEmailLength: Int? = null
    private var maxEmailLength: Int? = null

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)
    override val isUserAuthorized: LiveData<Boolean> = MutableLiveData<Boolean>().apply{
        authorizationManager
            .isUserLoggedIn
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    override val name = MutableLiveData<String>()
    override val nameError = MutableLiveData<IRegistrationUserPersonalInformationViewModel.NameError?>()
    override val surname = MutableLiveData<String>()
    override val surnameError = MutableLiveData<IRegistrationUserPersonalInformationViewModel.SurnameError?>()
    override val phoneNumber = MutableLiveData<String>()
    override val email = MutableLiveData<String>()
    override val emailError = MutableLiveData<IRegistrationUserPersonalInformationViewModel.EmailError?>()
    override val isCurrentUserPersonalInformationCorrect = MediatorLiveData<Boolean>().apply{
        val errorSources = listOf(
            nameError,
            surnameError,
            emailError
        )

        val onChanged: (Any?) -> Unit = {
            value = errorSources
                .map { it.value == null }
                .reduce { result, next -> result and next }
        }

        errorSources.forEach { addSource(it, onChanged) }
    }
    override val isUserPersonalInformationSaved = LiveEvent<Unit>()

    override val isAnyOperationInProgress = MutableLiveData<Boolean>().apply {
        activeOperationsCount
            .observeOn(mainThreadScheduler)
            .map { it > 0 }
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    init{
        prepareData()
    }

    override fun setViewModelToRegistrationFlow(authorizationToken: String) {
        isRegistrationFlow = true
        this.authorizationToken = authorizationToken
    }

    override fun updateName(name: String?) {
        nameError.value = checkName(name)
        if(this.name.value == name) return
        this.name.value = name
    }

    override fun updateSurname(surname: String?) {
        surnameError.value = checkSurname(surname)
        if(this.surname.value == surname) return
        this.surname.value = surname
    }

    override fun updateEmail(email: String?) {
        emailError.value = checkEmail(email)
        if(this.email.value == email) return
        this.email.value = email
    }

    override fun saveUserPersonalInformation() {
        if(isCurrentUserPersonalInformationCorrect.value != true) return

        val newUserPersonalInformation = collectUserPersonalInformation() ?: return

        saveUserPersonalDataDisposable?.dispose()

        var saveUserPersonalInformationCompletable = userPersonalInformationRepository
            .saveUserPersonalInformation(newUserPersonalInformation)

        if(isRegistrationFlow){
            val authorizationToken = this.authorizationToken
            if(authorizationToken != null){
                saveUserPersonalInformationCompletable = saveUserPersonalInformationCompletable
                    .andThen(authorizationManager.logIn(authorizationToken))
            }
        }

        saveUserPersonalDataDisposable = saveUserPersonalInformationCompletable
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doAfterTerminate(::decrementActiveOperationsCount)
            .subscribeBy(
                onComplete = { isUserPersonalInformationSaved.value = Unit },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun logOut() {
        logOutDisposable?.dispose()

        logOutDisposable = authorizationManager
            .logOut()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private fun prepareData(){
        dataPreparationDisposable?.dispose()

        dataPreparationDisposable = Completable
            .mergeArray(
                prepareUserPersonalInformationThresholds().andThen(prepareUserPersonalInformation())
            )
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .subscribeBy(
                onComplete = { dataPreparationState.value = ViewModelPreparationState.DataIsPrepared },
                onError = {
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData(it.message)
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    private fun prepareUserPersonalInformationThresholds(): Completable{
        return Completable
            .mergeArray(
                prepareMinNameLength(),
                prepareMaxNameLength(),
                prepareMinSurnameLength(),
                prepareMaxSurnameLength(),
                prepareMinEmailLength(),
                prepareMaxEmailLength()
            )
            .observeOn(mainThreadScheduler)
    }

    private fun prepareUserPersonalInformation(): Completable{
        return userPersonalInformationRepository
            .getUserPersonalInformation()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable{ userPersonalInformation ->
                Completable.fromRunnable {
                    updateName(userPersonalInformation.name)
                    updateSurname(userPersonalInformation.surname)
                    updatePhoneNumber(userPersonalInformation.phoneNumber)
                    updateEmail(userPersonalInformation.email)
                }
            }
    }

    private fun prepareMinNameLength(): Completable{
        return userPersonalInformationRepository
            .getMinUserNameLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { minNameLength = it }
            }
    }

    private fun prepareMaxNameLength(): Completable{
        return userPersonalInformationRepository
            .getMaxUserNameLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { maxNameLength = it }
            }
    }

    private fun prepareMinSurnameLength(): Completable{
        return userPersonalInformationRepository
            .getMinSurnameLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { minSurnameLength = it }
            }
    }

    private fun prepareMaxSurnameLength(): Completable{
        return userPersonalInformationRepository
            .getMaxSurnameLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { maxSurnameLength = it }
            }
    }

    private fun prepareMinEmailLength(): Completable{
        return userPersonalInformationRepository
            .getMinEmailLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { minEmailLength = it }
            }
    }

    private fun prepareMaxEmailLength(): Completable{
        return userPersonalInformationRepository
            .getMaxEmailLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { maxEmailLength = it }
            }
    }

    private fun updatePhoneNumber(phoneNumber: String){
        if(this.phoneNumber.value == phoneNumber) return
        this.phoneNumber.value = phoneNumber
    }

    private fun checkName(name: String?): IRegistrationUserPersonalInformationViewModel.NameError?{
        if(name.isNullOrEmpty()) return IRegistrationUserPersonalInformationViewModel.NameError.FieldIsRequired
        if(!name.checkContainsOnlyLetters()) return IRegistrationUserPersonalInformationViewModel.NameError.FieldContainsIllegalSymbols
        if(name.startsWith(' ')) return IRegistrationUserPersonalInformationViewModel.NameError.StartsWithWhitespace
        if(name.endsWith(' ')) return IRegistrationUserPersonalInformationViewModel.NameError.EndsWithWhitespace
        if(name.contains("\\s{2,}".toRegex())) return IRegistrationUserPersonalInformationViewModel.NameError.RepetitiveWhitespaces
        minNameLength?.let { if(name.length < it) return IRegistrationUserPersonalInformationViewModel.NameError.LengthIsTooShort(it) }
        maxNameLength?.let { if(name.length > it) return IRegistrationUserPersonalInformationViewModel.NameError.LengthIsTooLong(it) }

        return null
    }

    private fun checkSurname(surname: String?): IRegistrationUserPersonalInformationViewModel.SurnameError? {
        if(surname == null) return null
        if(!surname.checkContainsOnlyLetters()) return IRegistrationUserPersonalInformationViewModel.SurnameError.FieldContainsIllegalSymbols
        if(surname.startsWith(' ')) return IRegistrationUserPersonalInformationViewModel.SurnameError.StartsWithWhitespace
        if(surname.endsWith(' ')) return IRegistrationUserPersonalInformationViewModel.SurnameError.EndsWithWhitespace
        if(surname.contains("\\s{2,}".toRegex())) return IRegistrationUserPersonalInformationViewModel.SurnameError.RepetitiveWhitespaces
        minSurnameLength?.let { if(surname.length < it) return IRegistrationUserPersonalInformationViewModel.SurnameError.LengthIsTooShort(it) }
        maxSurnameLength?.let { if(surname.length > it) return IRegistrationUserPersonalInformationViewModel.SurnameError.LengthIsTooLong(it) }

        return null
    }

    private fun checkEmail(email: String?): IRegistrationUserPersonalInformationViewModel.EmailError? {
        if(email.isNullOrEmpty()) return IRegistrationUserPersonalInformationViewModel.EmailError.FieldIsRequired
        if(email.isNotEmail()) return IRegistrationUserPersonalInformationViewModel.EmailError.IllegalEmailFormat
        minEmailLength?.let { if(email.length < it) return IRegistrationUserPersonalInformationViewModel.EmailError.LengthIsTooShort(it) }
        maxEmailLength?.let { if(email.length > it) return IRegistrationUserPersonalInformationViewModel.EmailError.LengthIsTooLong(it) }

        return null
    }

    private fun String.checkContainsOnlyLetters(): Boolean{
        return Pattern.matches("^([а-яА-яёЁ ]*|[a-zA-Z ]*)$", this)
    }

    @Synchronized
    private fun incrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount + 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }

    @Synchronized
    private fun decrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCount.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount - 1

        activeOperationsCount.onNext(newActiveOperationsCount)
    }

    private fun collectUserPersonalInformation(): UserPersonalInformation?{
        val name = this.name.value
        val surname = this.surname.value
        val phoneNumber = this.phoneNumber.value ?: return null
        val email = this.email.value

        return UserPersonalInformation(name, surname, phoneNumber, email)
    }
}