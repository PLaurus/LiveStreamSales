package tv.wfc.livestreamsales.repository.authorization

import tv.wfc.livestreamsales.application.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.AuthorizedUserComponent
import tv.wfc.livestreamsales.model.application.phonenumberconfirmation.PhoneNumberConfirmationResult
import tv.wfc.livestreamsales.storage.authorization.local.IAuthorizationLocalStorage
import tv.wfc.livestreamsales.storage.authorization.remote.IAuthorizationRemoteStorage
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

    override fun sendConfirmationCodeRequest(phoneNumber: String): Single<Boolean> {
        if(isCodeRequestAvailable.hasValue() && isCodeRequestAvailable.value){
            return authorizationRemoteStorage.sendConfirmationCodeRequest(phoneNumber)
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

    override fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>{
        return authorizationRemoteStorage.confirmPhoneNumber(phoneNumber, confirmationCode)
            .doOnSuccess { phoneNumberConfirmationResult ->
                if(phoneNumberConfirmationResult is PhoneNumberConfirmationResult.PhoneNumberIsConfirmed){
                    authorizationLocalStorage.updateAuthorizationToken(phoneNumberConfirmationResult.token)
                        .observeOn(mainThreadScheduler)
                        .subscribeBy(onError = applicationErrorsLogger::logError)
                        .addTo(disposables)
                }
            }
    }

    override fun logOut(): Completable {
        return (authorizedUserComponent?.logOutRepository()?.logOut() ?: Completable.complete())
                .concatWith(authorizationLocalStorage.updateAuthorizationToken(null))
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