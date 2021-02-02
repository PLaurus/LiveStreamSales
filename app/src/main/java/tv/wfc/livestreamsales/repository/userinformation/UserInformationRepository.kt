package tv.wfc.livestreamsales.repository.userinformation

import tv.wfc.livestreamsales.application.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.userinformation.qualifiers.UserInformationLocalStorage
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.userinformation.qualifiers.UserInformationRemoteStorage
import tv.wfc.livestreamsales.model.application.user.UserInformation
import tv.wfc.livestreamsales.storage.userinformation.IUserInformationStorage
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class UserInformationRepository @Inject constructor(
    @UserInformationRemoteStorage
    private val userRemoteStorage: IUserInformationStorage,
    @UserInformationLocalStorage
    private val userLocalStorage: IUserInformationStorage,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): IUserInformationRepository {
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