package tv.wfc.livestreamsales.features.profile.viewmodel

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
import tv.wfc.livestreamsales.application.model.user.User
import tv.wfc.livestreamsales.application.repository.userinformation.IUserInformationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import java.util.regex.Pattern
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userInformationRepository: IUserInformationRepository,
    private val authorizationManager: IAuthorizationManager,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), IProfileViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCount = BehaviorSubject.createDefault(0)

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
    override val nameError = MutableLiveData<IProfileViewModel.NameError?>()
    override val surname = MutableLiveData<String>()
    override val surnameError = MutableLiveData<IProfileViewModel.SurnameError?>()
    override val phoneNumber = MutableLiveData<String>()
    override val email = MutableLiveData<String>()
    override val emailError = MutableLiveData<IProfileViewModel.EmailError?>()
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

        saveUserPersonalDataDisposable = userInformationRepository
            .saveUserInformation(newUserPersonalInformation)
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
        return userInformationRepository
            .getUserInformation()
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
        return userInformationRepository
            .getMinUserNameLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { minNameLength = it }
            }
    }

    private fun prepareMaxNameLength(): Completable{
        return userInformationRepository
            .getMaxUserNameLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { maxNameLength = it }
            }
    }

    private fun prepareMinSurnameLength(): Completable{
        return userInformationRepository
            .getMinSurnameLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { minSurnameLength = it }
            }
    }

    private fun prepareMaxSurnameLength(): Completable{
        return userInformationRepository
            .getMaxSurnameLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { maxSurnameLength = it }
            }
    }

    private fun prepareMinEmailLength(): Completable{
        return userInformationRepository
            .getMinEmailLength()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .flatMapCompletable {
                Completable.fromRunnable { minEmailLength = it }
            }
    }

    private fun prepareMaxEmailLength(): Completable{
        return userInformationRepository
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

    private fun checkName(name: String?): IProfileViewModel.NameError?{
        if(name.isNullOrEmpty()) return IProfileViewModel.NameError.FieldIsRequired
        if(!name.checkContainsOnlyLetters()) return IProfileViewModel.NameError.FieldContainsIllegalSymbols
        if(name.startsWith(' ')) return IProfileViewModel.NameError.StartsWithWhitespace
        if(name.endsWith(' ')) return IProfileViewModel.NameError.EndsWithWhitespace
        if(name.contains("\\s{2,}".toRegex())) return IProfileViewModel.NameError.RepetitiveWhitespaces
        minNameLength?.let { if(name.length < it) return IProfileViewModel.NameError.LengthIsTooShort(it) }
        maxNameLength?.let { if(name.length > it) return IProfileViewModel.NameError.LengthIsTooLong(it) }

        return null
    }

    private fun checkSurname(surname: String?): IProfileViewModel.SurnameError? {
        if(surname == null) return null
        if(!surname.checkContainsOnlyLetters()) return IProfileViewModel.SurnameError.FieldContainsIllegalSymbols
        if(surname.startsWith(' ')) return IProfileViewModel.SurnameError.StartsWithWhitespace
        if(surname.endsWith(' ')) return IProfileViewModel.SurnameError.EndsWithWhitespace
        if(surname.contains("\\s{2,}".toRegex())) return IProfileViewModel.SurnameError.RepetitiveWhitespaces
        minSurnameLength?.let { if(surname.length < it) return IProfileViewModel.SurnameError.LengthIsTooShort(it) }
        maxSurnameLength?.let { if(surname.length > it) return IProfileViewModel.SurnameError.LengthIsTooLong(it) }

        return null
    }

    private fun checkEmail(email: String?): IProfileViewModel.EmailError? {
        if(email.isNullOrEmpty()) return IProfileViewModel.EmailError.FieldIsRequired
        if(email.isNotEmail()) return IProfileViewModel.EmailError.IllegalEmailFormat
        minEmailLength?.let { if(email.length < it) return IProfileViewModel.EmailError.LengthIsTooShort(it) }
        maxEmailLength?.let { if(email.length > it) return IProfileViewModel.EmailError.LengthIsTooLong(it) }

        return null
    }

    private fun String.checkContainsOnlyLetters(): Boolean{
        return Pattern.matches("^([а-яА-я ]*|[a-zA-Z ]*)$", this)
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

    private fun collectUserPersonalInformation(): User?{
        val name = this.name.value
        val surname = this.surname.value
        val phoneNumber = this.phoneNumber.value ?: return null
        val email = this.email.value

        return User(name, surname, phoneNumber, email)
    }
}