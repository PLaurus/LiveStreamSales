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
    private val isUserLoggedInSubject = BehaviorSubject.createDefault(false)

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
        return authorizationRepository
            .confirmPhoneNumber(phoneNumber, confirmationCode)
            .subscribeOn(ioScheduler)
    }

    override fun logInTemporary(token: String): Completable {
        return Completable
            .fromRunnable {
                authorizationInterceptor.token = token
            }
            .doOnComplete{ isUserLoggedInSubject.onNext(true) }
            .subscribeOn(mainThreadScheduler)
    }

    override fun logIn(token: String): Completable {
        return logInTemporary(token)
            .mergeWith(saveTokenToLocalStorage(token))
            .subscribeOn(ioScheduler)
    }

    override fun logOut(): Completable {
        val logOutInterceptor = Completable
            .fromRunnable { authorizationInterceptor.token = null }
            .subscribeOn(mainThreadScheduler)

        return logOutInterceptor
            .mergeWith(saveTokenToLocalStorage(null))
            .observeOn(mainThreadScheduler)
            .doOnComplete{ isUserLoggedInSubject.onNext(false) }
    }

    private fun recoverDataFromStorage(){
        recoverAuthorizationState()
        recoverNextCodeWaitingTime()
    }

    private fun recoverAuthorizationState(){
        authorizationRepository.getCurrentAuthorizationToken()
            .toSingle()
            .flatMapCompletable(::logIn)
            .onErrorResumeWith{ logOut() }
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
            .subscribeBy(applicationErrorsLogger::logError)
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

    private fun saveTokenToLocalStorage(token: String?): Completable{
        return authorizationRepository
            .updateAuthorizationToken(token)
            .subscribeOn(ioScheduler)
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