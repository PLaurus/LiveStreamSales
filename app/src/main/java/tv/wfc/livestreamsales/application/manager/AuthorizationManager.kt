package tv.wfc.livestreamsales.application.manager

import android.util.Log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.rest.interceptors.IAuthorizationInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthorizationManager @Inject constructor(
    private val authorizationRepository: IAuthorizationRepository,
    private val authorizationInterceptor: IAuthorizationInterceptor,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    @ComputationScheduler
    private val computationScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IAuthorizationManager {
    private val disposables = CompositeDisposable()
    private val isUserLoggedInSubject = BehaviorSubject.create<Boolean>()

    private var codeRequestTimer: Disposable? = null

    override val isUserLoggedIn: Observable<Boolean> = isUserLoggedInSubject.distinctUntilChanged()
    override val nextCodeRequestWaitingTime: BehaviorSubject<Long> = BehaviorSubject.create()
    override val isCodeRequestAvailable: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(true).apply {
        nextCodeRequestWaitingTime
            .map{ it <= 0 }
            .distinctUntilChanged()
            .observeOn(ioScheduler)
            .subscribe(::onNext)
            .addTo(disposables)
    }

    init{
        recoverDataFromStorage()
        startSavingNextCodeRequestWaitingTime()
    }

    override fun checkIsUserLoggedIn(): Boolean {
        return isUserLoggedInSubject.value ?: false
    }

    override fun requestConfirmationCode(phoneNumber: String): Single<Boolean> {
        if(isCodeRequestAvailable.hasValue() && isCodeRequestAvailable.value){
            return authorizationRepository.requestConfirmationCode(phoneNumber)
                .doOnSuccess{
                    startCodeRequestTimer()
                }
        }

        return Single.just(false)
    }

    override fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>{
        return authorizationRepository.confirmPhoneNumber(phoneNumber, confirmationCode)
            .doOnSuccess { phoneNumberConfirmationResult ->
                if(phoneNumberConfirmationResult is PhoneNumberConfirmationResult.PhoneNumberIsConfirmed){
                    val token = phoneNumberConfirmationResult.token
                    updateAuthorizationState(token)
                }
            }
    }

    override fun logOut(): Completable {
        return authorizationRepository.logOut()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .doAfterTerminate { updateAuthorizationState(null) }
    }

    private fun recoverDataFromStorage(){
        recoverAuthorizationState()
        recoverNextCodeWaitingTime()
    }

    private fun recoverAuthorizationState(){
        authorizationRepository.getCurrentAuthorizationToken()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = ::updateAuthorizationState,
                onComplete = { updateAuthorizationState(null) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun recoverNextCodeWaitingTime(){
        authorizationRepository.getNextCodeRequestWaitingTime()
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = ::startCodeRequestTimer,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun updateAuthorizationState(token: String?){
        authorizationInterceptor.token = token
        isUserLoggedInSubject.onNext(!authorizationInterceptor.token.isNullOrBlank())
        saveTokenToLocalStorage(token)
    }

    private fun saveTokenToLocalStorage(token: String?){
        authorizationRepository.updateAuthorizationToken(token)
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onComplete = {
                    Log.d(this::class.java.simpleName, "Saved token to local storage.")
                },
                onError = applicationErrorsLogger::logError
            )
    }

    private fun startCodeRequestTimer() {
        val nextCodeRequestRequiredWaitingTime = authorizationRepository.getNextCodeRequestRequiredWaitingTime()

        nextCodeRequestRequiredWaitingTime
            .observeOn(ioScheduler)
            .subscribeBy(
                onSuccess = ::startCodeRequestTimer,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun startCodeRequestTimer(time: Long){
        codeRequestTimer?.dispose()

        codeRequestTimer = Observable
            .intervalRange(
                0L,
                time + 1,
                0L,
                1L,
                TimeUnit.SECONDS
            )
            .map{ time - it }
            .subscribeOn(computationScheduler)
            .observeOn(ioScheduler)
            .subscribe(nextCodeRequestWaitingTime::onNext)
            .addTo(disposables)
    }

    private fun startSavingNextCodeRequestWaitingTime(){
        nextCodeRequestWaitingTime
            .observeOn(ioScheduler)
            .subscribe(authorizationRepository::saveNextCodeRequestWaitingTime)
            .addTo(disposables)
    }
}