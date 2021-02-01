package com.example.livestreamsales.storage.authorization.remote

import com.example.livestreamsales.BuildConfig
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import com.example.livestreamsales.model.application.phonenumberconfirmation.PhoneNumberConfirmationResult
import com.example.livestreamsales.model.network.rest.request.ConfirmPhoneNumberRequestBody
import com.example.livestreamsales.model.network.rest.request.SendConfirmationCodeRequestRequestBody
import com.example.livestreamsales.network.rest.api.notauthorized.ILogInApi
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AuthorizationRemoteStorage @Inject constructor(
    private val logInApi: ILogInApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IAuthorizationRemoteStorage {
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
        val response = logInApi.sendConfirmationCodeRequest(sendRequestCodeRequestBody)

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
        val response = logInApi.getConfirmationCodeLength()

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
        val response = logInApi.getNextCodeRequestRequiredWaitingTime()

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
            return Single.just(PhoneNumberConfirmationResult.PhoneNumberIsConfirmed("1a2b3c4d"))
        }

        val confirmPhoneNumberRequestBody = ConfirmPhoneNumberRequestBody(phoneNumber, confirmationCode)
        val response = logInApi.confirmPhoneNumber(confirmPhoneNumberRequestBody)

        return response
            .flatMap{
                val body = it.body()

                if(body != null){
                    val errorMessage = body.errorMessage
                    val token = body.token

                    val phoneNumberConfirmationResult = if(token != null){
                        PhoneNumberConfirmationResult.PhoneNumberIsConfirmed(token)
                    } else{
                        PhoneNumberConfirmationResult.PhoneNumberIsNotConfirmed(errorMessage)
                    }

                    Single.just(phoneNumberConfirmationResult)
                } else{
                    Single.just(PhoneNumberConfirmationResult.PhoneNumberIsNotConfirmed())
                }
            }
            .subscribeOn(ioScheduler)
    }
}