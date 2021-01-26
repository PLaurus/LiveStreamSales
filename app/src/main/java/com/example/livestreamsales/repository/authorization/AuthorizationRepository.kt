package com.example.livestreamsales.repository.authorization

import com.example.livestreamsales.application.errors.IApplicationErrorsLogger
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.model.application.phoneconfirmation.PhoneConfirmationResult
import com.example.livestreamsales.storage.authorization.local.IAuthorizationLocalStorage
import com.example.livestreamsales.storage.authorization.remote.IAuthorizationRemoteStorage
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val authorizationRemoteStorage: IAuthorizationRemoteStorage,
    private val authorizationLocalStorage: IAuthorizationLocalStorage,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IAuthorizationRepository {
    private val disposables = CompositeDisposable()

    override val isUserLoggedIn: Observable<Boolean>
        get() = authorizationLocalStorage.isUserLoggedIn

    override val authorizedUserComponent: AuthorizedUserComponent?
        get() = authorizationLocalStorage.authorizedUserComponent

    override val nextCodeRequestWaitingTime: Observable<Long>
        get() = authorizationLocalStorage.nextCodeRequestWaitingTime

    override val isCodeRequestAvailable: BehaviorSubject<Boolean> = BehaviorSubject.create<Boolean>().apply{
        authorizationLocalStorage.isCodeRequestAvailable
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::onNext,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun sendVerificationCodeRequest(telephoneNumber: String): Single<Boolean> {
        if(isCodeRequestAvailable.hasValue() && isCodeRequestAvailable.value){
            return authorizationRemoteStorage.sendVerificationCodeRequest(telephoneNumber)
                .doOnSuccess{ isCodeSent ->
                    if(isCodeSent){
                        getAndSaveNextCodeRequestRequiredWaitingTimeFromRemote()
                            .ignoreElement()
                            .observeOn(mainThreadScheduler)
                            .subscribeBy(
                                onError = applicationErrorsLogger::logError,
                                onComplete = authorizationLocalStorage::startCodeRequestTimer
                            )
                    }
                }
        }

        return Single.just(false)
    }

    override fun getRequiredCodeLength(): Single<Int> {
        return getAndSaveRequiredCodeLengthFromRemote()
            .switchIfEmpty(authorizationLocalStorage.getRequiredCodeLength())
    }

    override fun getNextCodeRequestRequiredWaitingTime(): Single<Long> {
        return getAndSaveNextCodeRequestRequiredWaitingTimeFromRemote()
            .switchIfEmpty(authorizationLocalStorage.getNextCodeRequestRequiredWaitingTime())
    }

    override fun confirmPhone(phoneNumber: String, verificationCode: Int): Single<PhoneConfirmationResult>{
        return authorizationRemoteStorage.confirmPhone(phoneNumber, verificationCode)
            .doOnSuccess { phoneConfirmationResult ->
                if(phoneConfirmationResult is PhoneConfirmationResult.PhoneIsConfirmed){
                    authorizationLocalStorage.updateAuthorizationToken(phoneConfirmationResult.token)
                        .observeOn(mainThreadScheduler)
                        .subscribeBy(onError = applicationErrorsLogger::logError)
                        .addTo(disposables)
                }
            }
    }

    override fun logOut(): Completable {
        return (authorizedUserComponent?.userRepository()?.processDataOnLogOut() ?: Completable.complete())
                .concatWith(authorizationLocalStorage.updateAuthorizationToken(null))
                .observeOn(mainThreadScheduler)

    }

    private fun getAndSaveRequiredCodeLengthFromRemote(): Maybe<Int>{
        return authorizationRemoteStorage.getRequiredCodeLength()
            .doOnSuccess(::saveRequiredCodeLengthLocally)
    }

    private fun saveRequiredCodeLengthLocally(length: Int){
        authorizationLocalStorage.saveRequiredCodeLength(length)
            .observeOn(mainThreadScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun getAndSaveNextCodeRequestRequiredWaitingTimeFromRemote(): Maybe<Long>{
        return authorizationRemoteStorage.getNextCodeRequestRequiredWaitingTime()
            .doOnSuccess(::saveNextCodeRequestRequiredWaitingTimeLocally)
    }

    private fun saveNextCodeRequestRequiredWaitingTimeLocally(timeInSeconds: Long){
        authorizationLocalStorage.saveNextCodeRequestRequiredWaitingTime(timeInSeconds)
            .observeOn(mainThreadScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }
}