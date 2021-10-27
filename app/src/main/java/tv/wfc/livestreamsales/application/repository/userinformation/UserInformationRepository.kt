package tv.wfc.livestreamsales.application.repository.userinformation

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.application.di.modules.datasource.qualifiers.UserInformationLocalDataStore
import tv.wfc.livestreamsales.application.di.modules.datasource.qualifiers.UserInformationRemoteDataStore
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.user.User
import tv.wfc.livestreamsales.application.storage.userpersonalinformation.IUserInformationDataStore
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject

class UserInformationRepository @Inject constructor(
    @UserInformationRemoteDataStore
    private val userInformationRemoteDataStore: IUserInformationDataStore,
    @UserInformationLocalDataStore
    private val userInformationLocalDataStore: IUserInformationDataStore,
    @IoScheduler
    private val ioScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IUserInformationRepository {
    private var disposables = CompositeDisposable()

    private var isUserInformationSavedLocally = false

    override fun getMinUserNameLength(): Single<Int> {
        return userInformationRemoteDataStore.getMinUserNameLength()
            .onErrorResumeNext { userInformationLocalDataStore.getMinUserNameLength() }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxUserNameLength(): Single<Int> {
        return userInformationRemoteDataStore.getMaxUserNameLength()
            .onErrorResumeNext { userInformationLocalDataStore.getMaxUserNameLength() }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinUserNameLength(minLength: Int): Completable {
        return userInformationRemoteDataStore.updateMinUserNameLength(minLength)
            .doOnComplete {
                updateMinUserNameLengthLocally(minLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxUserNameLength(maxLength: Int): Completable {
        return userInformationRemoteDataStore.updateMaxUserNameLength(maxLength)
            .doOnComplete {
                updateMaxUserNameLengthLocally(maxLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMinSurnameLength(): Single<Int> {
        return userInformationRemoteDataStore.getMinSurnameLength()
            .onErrorResumeNext { userInformationLocalDataStore.getMinSurnameLength() }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxSurnameLength(): Single<Int> {
        return userInformationRemoteDataStore.getMaxSurnameLength()
            .onErrorResumeNext { userInformationLocalDataStore.getMaxSurnameLength() }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinSurnameLength(minLength: Int): Completable {
        return userInformationRemoteDataStore.updateMinSurnameLength(minLength)
            .doOnComplete {
                updateMinSurnameLengthLocally(minLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxSurnameLength(maxLength: Int): Completable {
        return userInformationRemoteDataStore.updateMaxSurnameLength(maxLength)
            .doOnComplete {
                updateMaxSurnameLengthLocally(maxLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMinEmailLength(): Single<Int> {
        return userInformationRemoteDataStore.getMinEmailLength()
            .onErrorResumeNext { userInformationLocalDataStore.getMinEmailLength() }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxEmailLength(): Single<Int> {
        return userInformationRemoteDataStore.getMaxEmailLength()
            .onErrorResumeNext { userInformationLocalDataStore.getMaxEmailLength() }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinEmailLength(minLength: Int): Completable {
        return userInformationRemoteDataStore.updateMinEmailLength(minLength)
            .doOnComplete {
                updateMinEmailLengthLocally(minLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxEmailLength(maxLength: Int): Completable {
        return userInformationRemoteDataStore.updateMaxEmailLength(maxLength)
            .doOnComplete {
                updateMaxEmailLengthLocally(maxLength)
            }
            .subscribeOn(ioScheduler)
    }

    override fun getUserInformation(): Observable<User>{
        return if(!isUserInformationSavedLocally){
            getAndSaveUserInformationFromRemote().toObservable()
        } else{
            Observable.concat(
                userInformationLocalDataStore.getUserInformation().toObservable(),
                getAndSaveUserInformationFromRemote().toObservable()
            )
        }
    }

    override fun saveUserInformation(user: User): Completable {
        return userInformationRemoteDataStore.saveUserInformation(user)
            .doOnComplete { saveUserInformationLocally(user) }
    }

    private fun updateMinUserNameLengthLocally(minLength: Int){
        userInformationLocalDataStore.updateMinUserNameLength(minLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMaxUserNameLengthLocally(maxLength: Int){
        userInformationLocalDataStore.updateMaxUserNameLength(maxLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMaxSurnameLengthLocally(maxLength: Int){
        userInformationLocalDataStore.updateMaxSurnameLength(maxLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMinSurnameLengthLocally(minLength: Int){
        userInformationLocalDataStore.updateMinSurnameLength(minLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMaxEmailLengthLocally(maxLength: Int){
        userInformationLocalDataStore.updateMaxEmailLength(maxLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun updateMinEmailLengthLocally(minLength: Int){
        userInformationLocalDataStore.updateMinEmailLength(minLength)
            .observeOn(ioScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun getAndSaveUserInformationFromRemote(): Single<User>{
        return userInformationRemoteDataStore.getUserInformation()
            .doOnSuccess { userInformation ->
                saveUserInformationLocally(userInformation)
            }
    }

    @Synchronized
    private fun saveUserInformationLocally(user: User){
        userInformationLocalDataStore.saveUserInformation(user)
            .observeOn(ioScheduler)
            .subscribeBy(
                onComplete = { isUserInformationSavedLocally = true },
                onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }
}