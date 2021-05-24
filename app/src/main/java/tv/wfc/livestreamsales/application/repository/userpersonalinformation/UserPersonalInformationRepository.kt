package tv.wfc.livestreamsales.application.repository.userpersonalinformation

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.UserInformationLocalStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.UserInformationRemoteStorage
import tv.wfc.livestreamsales.application.model.userpersonalinformation.UserPersonalInformation
import tv.wfc.livestreamsales.application.storage.userpersonalinformation.IUserPersonalInformationStorage
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class UserPersonalInformationRepository @Inject constructor(
    @UserInformationRemoteStorage
    private val userPersonalInformationRemoteStorage: IUserPersonalInformationStorage,
    @UserInformationLocalStorage
    private val userPersonalInformationLocalStorage: IUserPersonalInformationStorage,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IUserPersonalInformationRepository {
    private var disposables = CompositeDisposable()

    private var isUserInformationSavedLocally = false

    override fun getMinUserNameLength(): Single<Int> {
        return userPersonalInformationRemoteStorage.getMinUserNameLength()
            .onErrorResumeNext { userPersonalInformationLocalStorage.getMinUserNameLength() }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxUserNameLength(): Single<Int> {
        return userPersonalInformationRemoteStorage.getMaxUserNameLength()
            .onErrorResumeNext { userPersonalInformationLocalStorage.getMaxUserNameLength() }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinUserNameLength(minLength: Int): Completable {
        return userPersonalInformationRemoteStorage.updateMinUserNameLength(minLength)
            .doOnComplete {
                updateMinUserNameLengthLocally(minLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxUserNameLength(maxLength: Int): Completable {
        return userPersonalInformationRemoteStorage.updateMaxUserNameLength(maxLength)
            .doOnComplete {
                updateMaxUserNameLengthLocally(maxLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMinSurnameLength(): Single<Int> {
        return userPersonalInformationRemoteStorage.getMinSurnameLength()
            .onErrorResumeNext { userPersonalInformationLocalStorage.getMinSurnameLength() }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxSurnameLength(): Single<Int> {
        return userPersonalInformationRemoteStorage.getMaxSurnameLength()
            .onErrorResumeNext { userPersonalInformationLocalStorage.getMaxSurnameLength() }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinSurnameLength(minLength: Int): Completable {
        return userPersonalInformationRemoteStorage.updateMinSurnameLength(minLength)
            .doOnComplete {
                updateMinSurnameLengthLocally(minLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxSurnameLength(maxLength: Int): Completable {
        return userPersonalInformationRemoteStorage.updateMaxSurnameLength(maxLength)
            .doOnComplete {
                updateMaxSurnameLengthLocally(maxLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMinEmailLength(): Single<Int> {
        return userPersonalInformationRemoteStorage.getMinEmailLength()
            .onErrorResumeNext { userPersonalInformationLocalStorage.getMinEmailLength() }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxEmailLength(): Single<Int> {
        return userPersonalInformationRemoteStorage.getMaxEmailLength()
            .onErrorResumeNext { userPersonalInformationLocalStorage.getMaxEmailLength() }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinEmailLength(minLength: Int): Completable {
        return userPersonalInformationRemoteStorage.updateMinEmailLength(minLength)
            .doOnComplete {
                updateMinEmailLengthLocally(minLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxEmailLength(maxLength: Int): Completable {
        return userPersonalInformationRemoteStorage.updateMaxEmailLength(maxLength)
            .doOnComplete {
                updateMaxEmailLengthLocally(maxLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun getUserPersonalInformation(): Observable<UserPersonalInformation>{
        return if(!isUserInformationSavedLocally){
            getAndSaveUserInformationFromRemote().toObservable()
        } else{
            Observable.concat(
                userPersonalInformationLocalStorage.getUserPersonalInformation().toObservable(),
                getAndSaveUserInformationFromRemote().toObservable()
            )
        }
    }

    override fun saveUserPersonalInformation(userPersonalInformation: UserPersonalInformation): Completable {
        return userPersonalInformationRemoteStorage.saveUserPersonalInformation(userPersonalInformation)
            .doOnComplete { saveUserInformationLocally(userPersonalInformation) }
    }

    private fun updateMinUserNameLengthLocally(minLength: Int){
        userPersonalInformationLocalStorage.updateMinUserNameLength(minLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMaxUserNameLengthLocally(maxLength: Int){
        userPersonalInformationLocalStorage.updateMaxUserNameLength(maxLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMaxSurnameLengthLocally(maxLength: Int){
        userPersonalInformationLocalStorage.updateMaxSurnameLength(maxLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMinSurnameLengthLocally(minLength: Int){
        userPersonalInformationLocalStorage.updateMinSurnameLength(minLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMaxEmailLengthLocally(maxLength: Int){
        userPersonalInformationLocalStorage.updateMaxEmailLength(maxLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMinEmailLengthLocally(minLength: Int){
        userPersonalInformationLocalStorage.updateMinEmailLength(minLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun getAndSaveUserInformationFromRemote(): Single<UserPersonalInformation>{
        return userPersonalInformationRemoteStorage.getUserPersonalInformation()
            .doOnSuccess { userInformation ->
                saveUserInformationLocally(userInformation)
            }
    }

    @Synchronized
    private fun saveUserInformationLocally(userPersonalInformation: UserPersonalInformation){
        userPersonalInformationLocalStorage.saveUserPersonalInformation(userPersonalInformation)
            .observeOn(ioScheduler)
            .subscribeBy(
                onComplete = { isUserInformationSavedLocally = true },
                onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }
}