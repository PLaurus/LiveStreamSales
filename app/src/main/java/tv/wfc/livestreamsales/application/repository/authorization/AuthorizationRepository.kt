package tv.wfc.livestreamsales.application.repository.authorization

import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult
import tv.wfc.livestreamsales.application.storage.authorization.local.IAuthorizationLocalStorage
import tv.wfc.livestreamsales.application.storage.authorization.remote.IAuthorizationRemoteStorage
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val authorizationRemoteStorage: IAuthorizationRemoteStorage,
    private val authorizationLocalStorage: IAuthorizationLocalStorage,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IAuthorizationRepository {
    private val disposables = CompositeDisposable()

    override fun getCurrentAuthorizationToken(): Maybe<String> {
        return authorizationLocalStorage
            .getAuthorizationToken()
            .subscribeOn(ioScheduler)
    }

    override fun updateAuthorizationToken(token: String?): Completable {
        return authorizationLocalStorage
            .updateAuthorizationToken(token)
            .subscribeOn(ioScheduler)
    }

    override fun requestConfirmationCode(phoneNumber: String): Single<Boolean> {
        return authorizationRemoteStorage.sendConfirmationCodeRequest(phoneNumber)
    }

    override fun getRequiredCodeLength(): Single<Int> {
        return authorizationLocalStorage.getRequiredCodeLength()
    }

    override fun getNextCodeRequestRequiredWaitingTime(): Single<Long> {
        return authorizationLocalStorage.getNextCodeRequestMaxWaitingTime()
    }

    override fun getNextCodeRequestWaitingTime(): Single<Long> {
        return authorizationLocalStorage.getNextCodeRequestWaitingTime()
    }

    override fun saveNextCodeRequestWaitingTime(leftTimeToWaitInSeconds: Long): Completable {
        return authorizationLocalStorage.saveNextCodeRequestWaitingTime(leftTimeToWaitInSeconds)
    }

    override fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>{
        return authorizationRemoteStorage.confirmPhoneNumber(phoneNumber, confirmationCode)
            .subscribeOn(ioScheduler)
    }

    override fun logOut(): Completable {
        return authorizationRemoteStorage.logOut()
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

    private fun getAndSaveNextCodeRequestMaxWaitingTimeFromRemote(): Maybe<Long>{
        return authorizationRemoteStorage.getNextCodeRequestRequiredWaitingTime()
            .doOnSuccess(::saveNextCodeRequestMaxWaitingTimeLocally)
    }

    private fun saveNextCodeRequestMaxWaitingTimeLocally(timeInSeconds: Long){
        authorizationLocalStorage.saveNextCodeRequestMaxWaitingTime(timeInSeconds)
            .observeOn(mainThreadScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }
}