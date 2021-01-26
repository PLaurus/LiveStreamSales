package com.example.livestreamsales.storage.authorization.remote

import com.example.livestreamsales.BuildConfig
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import com.example.livestreamsales.model.application.phoneconfirmation.PhoneConfirmationResult
import com.example.livestreamsales.model.network.rest.request.SendVerificationCodeRequestRequestBody
import com.example.livestreamsales.model.network.rest.request.VerifyPhoneNumberRequestBody
import com.example.livestreamsales.network.rest.api.IAuthorizationApi
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AuthorizationRemoteStorage @Inject constructor(
    private val authorizationApi: IAuthorizationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IAuthorizationRemoteStorage {
    /**
     * Requests the remote storage to send verification code in an sms.
     * Operates by default on an IoScheduler.
     * @param telephoneNumber phone number which user want to confirm.
     * @return The new Single that emits whether code is sent
     */
    override fun sendVerificationCodeRequest(telephoneNumber: String): Single<Boolean> {
        if(BuildConfig.IgnoreRestRequests){
            return Single.just(true)
        }

        val sendRequestCodeRequestBody = SendVerificationCodeRequestRequestBody(telephoneNumber)
        val response = authorizationApi.sendVerificationCodeRequest(sendRequestCodeRequestBody)

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
        val response = authorizationApi.getVerificationCodeLength()

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
     * Requests server to verify phone number by the specified verification code.
     * Operates by default on an IoScheduler.
     * @param phoneNumber phone number which user want to confirm.
     * @param verificationCode the code that was received in an sms by a user.
     * @return The new Maybe that emits verification result.
     * If error occurred during communication Maybe emits nothing and then just completes
     */
    override fun confirmPhone(phoneNumber: String, verificationCode: Int): Single<PhoneConfirmationResult>{
        if(BuildConfig.IgnoreRestRequests){
            return Single.just(PhoneConfirmationResult.PhoneIsConfirmed("1a2b3c4d"))
        }

        val verifyPhoneNumberRequestBody = VerifyPhoneNumberRequestBody(phoneNumber, verificationCode)
        val response = authorizationApi.verifyPhoneNumber(verifyPhoneNumberRequestBody)

        return response
            .flatMap{
                val body = it.body()

                if(body != null){
                    val errorMessage = body.errorMessage
                    val token = body.token

                    val phoneConfirmationResult = if(token != null){
                        PhoneConfirmationResult.PhoneIsConfirmed(token)
                    } else{
                        PhoneConfirmationResult.PhoneIsNotConfirmed(errorMessage)
                    }

                    Single.just(phoneConfirmationResult)
                } else{
                    Single.just(PhoneConfirmationResult.PhoneIsNotConfirmed())
                }
            }
            .subscribeOn(ioScheduler)
    }
}