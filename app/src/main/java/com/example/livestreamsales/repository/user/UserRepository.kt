package com.example.livestreamsales.repository.user

import com.example.livestreamsales.application.errors.IApplicationErrorsLogger
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import com.example.livestreamsales.di.components.authorizeduser.modules.userinformation.qualifiers.UserInformationLocalStorage
import com.example.livestreamsales.di.components.authorizeduser.modules.userinformation.qualifiers.UserInformationRemoteStorage
import com.example.livestreamsales.model.application.user.UserInformation
import com.example.livestreamsales.storage.userinformation.IUserStorage
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class UserRepository @Inject constructor(
    @UserInformationRemoteStorage
    private val userRemoteStorage: IUserStorage,
    @UserInformationLocalStorage
    private val userLocalStorage: IUserStorage,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IUserRepository {
    private var disposables = CompositeDisposable()

    private var isUserInformationSavedLocally = false

    override fun getMinUserNameLength(): Single<Int> {
        return userRemoteStorage.getMinUserNameLength()
            .onErrorResumeNext { userLocalStorage.getMinUserNameLength() }
    }

    override fun getMaxUserNameLength(): Single<Int> {
        return userRemoteStorage.getMaxUserNameLength()
            .onErrorResumeNext { userLocalStorage.getMaxUserNameLength() }
    }

    override fun setMinUserNameLength(minLength: Int): Completable {
        return userRemoteStorage.setMinUserNameLength(minLength)
            .doOnComplete {
                setMinUserNameLengthLocally(minLength)
            }
    }

    override fun setMaxUserNameLength(maxLength: Int): Completable {
        return userRemoteStorage.setMaxUserNameLength(maxLength)
            .doOnComplete {
                setMaxUserNameLengthLocally(maxLength)
            }
    }

    override fun getUserInformation(): Observable<UserInformation>{
        return if(!isUserInformationSavedLocally){
            getAndSaveUserInformationFromRemote().toObservable()
        } else{
            Observable.concat(
                userLocalStorage.getUserInformation().toObservable(),
                getAndSaveUserInformationFromRemote().toObservable()
            )
        }
    }

    override fun saveUserInformation(userInformation: UserInformation): Single<Boolean> {
        return userRemoteStorage.saveUserInformation(userInformation)
            .doOnSuccess { isSavedRemote ->
                if(isSavedRemote){
                    saveUserInformationLocally(userInformation)
                }
            }
    }

    override fun processDataOnLogOut(): Completable {
        return Completable
            .mergeArrayDelayError(
                userLocalStorage.processDataOnLogout()
                    .doOnError(applicationErrorsLogger::logError),
                userRemoteStorage.processDataOnLogout()
                    .doOnError(applicationErrorsLogger::logError)
            )

    }

    private fun setMinUserNameLengthLocally(minLength: Int){
        userLocalStorage.setMinUserNameLength(minLength)
            .observeOn(mainThreadScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun setMaxUserNameLengthLocally(maxLength: Int){
        userLocalStorage.setMaxUserNameLength(maxLength)
            .observeOn(mainThreadScheduler)
            .subscribeBy(onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun getAndSaveUserInformationFromRemote(): Maybe<UserInformation>{
        return userRemoteStorage.getUserInformation()
            .doOnSuccess { userInformation ->
                saveUserInformationLocally(userInformation)
            }
    }

    private fun saveUserInformationLocally(userInformation: UserInformation){
        userLocalStorage.saveUserInformation(userInformation)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onSuccess = { isSaved ->
                    if(isSaved){
                        isUserInformationSavedLocally = true
                    } else{
                        applicationErrorsLogger.logError(
                            Exception("Failed to save user information in local storage")
                        )
                    }
                },
                onError = applicationErrorsLogger::logError)
            .addTo(disposables)
    }
}