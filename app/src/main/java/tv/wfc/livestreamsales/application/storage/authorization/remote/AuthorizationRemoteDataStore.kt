package tv.wfc.livestreamsales.application.storage.authorization.remote

import io.reactivex.rxjava3.core.Completable
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.phonenumberconfirmation.PhoneNumberConfirmationResult
import tv.wfc.livestreamsales.features.rest.model.api.confirmphonenumber.ConfirmPhoneNumberRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.sendconfirmationcoderequest.SendConfirmationCodeRequestRequestBody
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IAuthorizationApi
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AuthorizationRemoteDataStore @Inject constructor(
    private val authorizationApi: IAuthorizationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IAuthorizationRemoteDataStore {
    /**
     * Requests the remote storage to send confirmation code in an sms.
     * Operates by default on an IoScheduler.
     * @param phoneNumber phone number which user want to confirm.
     * @return The new Single that emits whether code is sent
     */
    override fun sendConfirmationCodeRequest(phoneNumber: String): Single<Boolean> {
        if(BuildConfig.IgnoreRestRequests){
            return Single.just(true)
        }

        val sendRequestCodeRequestBody = SendConfirmationCodeRequestRequestBody(phoneNumber)
        val response = authorizationApi.sendConfirmationCodeRequest(sendRequestCodeRequestBody)

        return response
            .flatMap{
                val body = it.body()

                if(body != null){
                    Single.just(body.isCodeSent)
                } else{
                    Single.just(false)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getRequiredCodeLength(): Maybe<Int> {
        val response = authorizationApi.getConfirmationCodeLength()

        return response
            .filter{ it.isSuccessful }
            .flatMap {
                val body = it.body()

                if(body != null){
                    Maybe.just(body.codeLength)
                } else Maybe.empty()
            }
            .subscribeOn(ioScheduler)
    }

    override fun getNextCodeRequestRequiredWaitingTime(): Maybe<Long> {
        val response = authorizationApi.getNextCodeRequestRequiredWaitingTime()

        return response
            .filter{ it.isSuccessful }
            .flatMap {
                val body = it.body()

                if(body != null){
                    Maybe.just(body.timeInSeconds)
                } else Maybe.empty()
            }
            .subscribeOn(ioScheduler)
    }

    /**
     * Requests server to confirm phone number by the specified confirmation code.
     * Operates by default on an IoScheduler.
     * @param phoneNumber phone number which user want to confirm.
     * @param confirmationCode the code that was received in an sms by a user.
     * @return The new Maybe that emits confirmation result.
     * If error occurred during communication Maybe emits nothing and then just completes
     */
    override fun confirmPhoneNumber(phoneNumber: String, confirmationCode: Int): Single<PhoneNumberConfirmationResult>{
        if(BuildConfig.IgnoreRestRequests){
            return Single.just(PhoneNumberConfirmationResult.Confirmed("1a2b3c4d"))
        }

        val confirmPhoneNumberRequestBody = ConfirmPhoneNumberRequestBody(phoneNumber, confirmationCode)
        val response = authorizationApi.confirmPhoneNumber(confirmPhoneNumberRequestBody)

        return response
            .flatMap{
                val body = it.body()

                if(body != null){
                    val errorMessage = body.errorMessage
                    val token = body.token
                    val needUserPersonalInformation = body.isProfileEmpty ?: false

                    val phoneNumberConfirmationResult = when{
                        token != null && needUserPersonalInformation -> PhoneNumberConfirmationResult.ConfirmedButNeedUserPersonalInformation(token)
                        token != null -> PhoneNumberConfirmationResult.Confirmed(token)
                        else -> PhoneNumberConfirmationResult.NotConfirmed(errorMessage)
                    }

                    Single.just(phoneNumberConfirmationResult)
                } else{
                    Single.just(PhoneNumberConfirmationResult.NotConfirmed())
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun logOut(): Completable {
        return authorizationApi
            .logOut()
            .ignoreElement()
            .subscribeOn(ioScheduler)
    }
}