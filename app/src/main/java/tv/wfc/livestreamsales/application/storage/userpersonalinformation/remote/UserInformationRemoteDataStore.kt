package tv.wfc.livestreamsales.application.storage.userpersonalinformation.remote

import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.user.User
import tv.wfc.livestreamsales.features.rest.model.api.updateuserpersonalinformation.UpdateUserPersonalInformationRequestBody
import tv.wfc.livestreamsales.features.rest.api.authorized.IUserPersonalInformationApi
import tv.wfc.livestreamsales.application.storage.userpersonalinformation.IUserInformationDataStore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.exception.storage.FailedToUpdateDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.NoSuchDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import javax.inject.Inject

class UserInformationRemoteDataStore @Inject constructor(
    private val userPersonalInformationApi: IUserPersonalInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IUserInformationDataStore {
    override fun getMinUserNameLength(): Single<Int> {
        // TODO: get user name min length from api, when such rest api is implemented
        return Single
            .fromCallable<Int> {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxUserNameLength(): Single<Int> {
        // TODO: get user name min length from api, when such rest api is implemented
        return Single
            .fromCallable<Int> {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinUserNameLength(minLength: Int): Completable {
        // TODO: update user name min length by api, when such rest api is implemented
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .observeOn(ioScheduler)
    }

    override fun updateMaxUserNameLength(maxLength: Int): Completable {
        // TODO: update user name max length by api, when such rest api is implemented
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMinSurnameLength(): Single<Int> {
        // TODO: get surname min length from api, when such rest api is implemented
        return Single
            .fromCallable<Int> { throw NotImplementedError() }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxSurnameLength(): Single<Int> {
        // TODO: get surname max length from api, when such rest api is implemented
        return Single
            .fromCallable<Int> { throw NotImplementedError() }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinSurnameLength(minLength: Int): Completable {
        // TODO: update surname min length by api, when such rest api is implemented
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxSurnameLength(maxLength: Int): Completable {
        // TODO: update surname max length by api, when such rest api is implemented
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getMinEmailLength(): Single<Int> {
        // TODO: get email min length from api, when such rest api is implemented
        return Single
            .fromCallable<Int> { throw NotImplementedError() }
            .subscribeOn(ioScheduler)
    }

    override fun getMaxEmailLength(): Single<Int> {
        // TODO: get email max length from api, when such rest api is implemented
        return Single
            .fromCallable<Int> { throw NotImplementedError() }
            .subscribeOn(ioScheduler)
    }

    override fun updateMinEmailLength(minLength: Int): Completable {
        // TODO: update email min length by api, when such rest api is implemented
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateMaxEmailLength(maxLength: Int): Completable {
        // TODO: update email max length by api, when such rest api is implemented
        return Completable
            .fromRunnable {
                throw NotImplementedError()
            }
            .subscribeOn(ioScheduler)
    }

    /**
     * Gets user information from a remote storage.
     * Operates by default on an IoScheduler.
     * @return the new Maybe that emits user personal information.
     * If error occurred during client-server communication emits nothing.
     */
    override fun getUserInformation(): Single<User> {
        if(BuildConfig.IgnoreRestRequests){
            return Single.just(
                User(null, null, "+79281049990", null)
            )
        }

        return userPersonalInformationApi
            .getUserPersonalInformation()
            .map{ it.data ?: throw NoSuchDataInStorageException() }
            .map{ remoteUserPersonalInformation ->
                remoteUserPersonalInformation.toApplicationUser() ?: throw ReceivedDataWithWrongFormatException()
            }
            .subscribeOn(ioScheduler)
    }

    /**
     * Saves user information to a remote storage.
     * Operates by default on an IoScheduler.
     * @param user - user personal data
     * @return the new Single that emits save operation result.
     */
    override fun saveUserInformation(user: User): Completable {
        val name = user.name
        val surname = user.surname
        val email = user.email
        val updateUserInformationRequestBody = UpdateUserPersonalInformationRequestBody(
            name,
            surname,
            email
        )

        val response = userPersonalInformationApi.updateUserPersonalInformation(updateUserInformationRequestBody)

        return response
            .map{ it.isInformationUpdatedSuccessfully ?: throw ReceivedDataWithWrongFormatException() }
            .flatMapCompletable{ isInformationUpdated ->
                if(isInformationUpdated) {
                    Completable.complete()
                } else{
                    Completable.error(FailedToUpdateDataInStorageException())
                }
            }
            .subscribeOn(ioScheduler)
    }

    private fun tv.wfc.livestreamsales.features.rest.model.api.getuserpersonalinformation.UserPersonalInformation.toApplicationUser(): User?{
        val name = name
        val surname = surname
        val phoneNumber = phoneNumber ?: return null
        val email = email

        return User(
            name,
            surname,
            phoneNumber,
            email
        )
    }
}