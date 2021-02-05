package tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.remote

import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.features.authorizeduser.model.user.UserInformation
import tv.wfc.livestreamsales.features.rest.model.api.request.UpdateUserInformationRequestBody
import tv.wfc.livestreamsales.features.rest.api.authorized.IUserInformationApi
import tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.IUserInformationStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class UserInformationRemoteStorage @Inject constructor(
    private val userInformationApi: IUserInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IUserInformationStorage {
    override fun setMinUserNameLength(minLength: Int): Completable {
        return Completable.fromCallable {
            throw NotImplementedError()
        }
    }

    override fun setMaxUserNameLength(maxLength: Int): Completable {
        return Completable.fromCallable {
            throw NotImplementedError()
        }
    }

    override fun getMinUserNameLength(): Single<Int> {
        // TODO: get user name min length from api, when such rest api is implemented
        return Single.fromCallable {
            throw NotImplementedError()
        }
    }

    override fun getMaxUserNameLength(): Single<Int> {
        // TODO: get user name min length from api, when such rest api is implemented
        return Single.fromCallable {
            throw NotImplementedError()
        }
    }

    /**
     * Gets user information from a remote storage.
     * Operates by default on an IoScheduler.
     * @return the new Maybe that emits user personal information.
     * If error occurred during client-server communication emits nothing.
     */
    override fun getUserInformation(): Maybe<UserInformation> {
        if(BuildConfig.IgnoreRestRequests){
            return Maybe.just(
                UserInformation("", null, "+79281049990", null)
            )
        }

        val response = userInformationApi.getUserInformation()

        return response
            .filter{ it.isSuccessful }
            .flatMap{
                val body = it.body()

                if(body != null){
                    val name = body.name
                    val surname = body.surname
                    val phoneNumber = body.phoneNumber
                    val email = body.email
                    val userInformation = UserInformation(name, surname, phoneNumber, email)

                    Maybe.just(userInformation)
                } else Maybe.empty()
            }
            .subscribeOn(ioScheduler)
    }

    /**
     * Saves user information to a remote storage.
     * Operates by default on an IoScheduler.
     * @param userInformation - user personal data
     * @return the new Single that emits save operation result.
     */
    override fun saveUserInformation(userInformation: UserInformation): Single<Boolean> {
        val name = userInformation.name
        val surname = userInformation.surname
        val email = userInformation.email
        val updateUserInformationRequestBody = UpdateUserInformationRequestBody(
            name,
            surname,
            email
        )

        val response = userInformationApi.updateUserInformation(updateUserInformationRequestBody)

        return response
            .flatMap{
                val body = it.body()

                if(!it.isSuccessful || body == null){
                    Single.just(false)
                }
                else{
                    Single.just(body.isInformationUpdatedSuccessfully)
                }
            }
            .subscribeOn(ioScheduler)
    }
}