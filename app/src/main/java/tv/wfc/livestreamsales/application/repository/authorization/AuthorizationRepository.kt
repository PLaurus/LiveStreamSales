package tv.wfc.livestreamsales.application.repository.authorization

import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult
import tv.wfc.livestreamsales.application.storage.authorization.local.IAuthorizationLocalDataStore
import tv.wfc.livestreamsales.application.storage.authorization.remote.IAuthorizationRemoteDataStore
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val authorizationRemoteDataStore: IAuthorizationRemoteDataStore,
    private val authorizationLocalDataStore: IAuthorizationLocalDataStore,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IAuthorizationRepository {
    private val disposables = CompositeDisposable()

    override fun getCurrentAuthorizationToken(): Maybe<String> {
        return authorizationLocalDataStore
            .getAuthorizationToken()
            .subscribeOn(ioScheduler)
    }

    override fun updateAuthorizationToken(token: String?): Completable {
        return authorizationLocalDataStore
            .updateAuthorizationToken(token)
            .subscribeOn(ioScheduler)
    }

    override fun requestConfirmationCode(phoneNumber: String): Single<Boolean> {
        return authorizationRemoteDataStore.sendConfirmationCodeRequest(phoneNumber)
    }

    override fun getRequiredCodeLength(): Single<Int> {
        return authorizationLocalDataStore.getRequiredCodeLength()
    }

    override fun getNextCodeRequestRequiredWaitingTime(): Single<Long> {
        return authorizationLocalDataStore.getNextCodeRequestMaxWaitingTime()
    }

    override fun getNextCodeRequestWaitingTime(): Single<Long> {
        return authorizationLocalDataStore.getNextCodeRequestWaitingTime()
    }

    override fun saveNextCodeRequestWaitingTime(leftTimeToWaitInSeconds: Long): Completable {
        return authorizationLocalDataStore.saveNextCodeRequestWaitingTime(leftTimeToWaitInSeconds)
    }

    override fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>{
        return authorizationRemoteDataStore.confirmPhoneNumber(phoneNumber, confirmationCode)
            .subscribeOn(ioScheduler)
    }

    override fun logOut(): Completable {
        return authorizationRemoteDataStore.logOut()
    }

    private fun getAndSaveRequiredCodeLengthFromRemote(): Maybe<Int>{
        return authorizationRemoteDataStore.getRequiredCodeLength()
            .doOnSuccess(::saveRequiredCodeLengthLocally)
    }

    private fun saveRequiredCodeLengthLocally(length: Int){
        authorizationLocalDataStore.saveRequiredCodeLength(length)
            .observeOn(mainThreadScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun getAndSaveNextCodeRequestMaxWaitingTimeFromRemote(): Maybe<Long>{
        return authorizationRemoteDataStore.getNextCodeRequestRequiredWaitingTime()
            .doOnSuccess(::saveNextCodeRequestMaxWaitingTimeLocally)
    }

    private fun saveNextCodeRequestMaxWaitingTimeLocally(timeInSeconds: Long){
        authorizationLocalDataStore.saveNextCodeRequestMaxWaitingTime(timeInSeconds)
            .observeOn(mainThreadScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }
}