package com.example.livestreamsales.storage.authorization.remote

import com.example.livestreamsales.BuildConfig
import com.example.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import com.example.livestreamsales.model.application.authorization.PhoneNumberVerificationResult
import com.example.livestreamsales.model.network.rest.request.SendVerificationCodeRequestRequestBody
import com.example.livestreamsales.model.network.rest.request.VerifyPhoneNumberRequestBody
import com.example.livestreamsales.network.rest.api.IAuthorizationApi
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class AuthorizationRemoteStorage @Inject constructor(
    private val authorizationApi: IAuthorizationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IAuthorizationRemoteStorage {
    /**
     * Requests the remote storage to send verification code in an sms.
     * Operates by default on an IoScheduler.
     * @param telephoneNumber - phone number which user want to confirm.
     * @return the new Maybe that emits whether code is sent. If error occurred during
     * communication Maybe emits nothing and then just completes
     */
    override fun sendVerificationCodeRequest(telephoneNumber: String): Maybe<Boolean> {
        if(BuildConfig.IgnoreRestRequests){
            return Maybe.just(true)
        }

        val sendRequestCodeRequestBody = SendVerificationCodeRequestRequestBody(telephoneNumber)
        val response = authorizationApi.sendVerificationCodeRequest(sendRequestCodeRequestBody)

        return response
            .filter{ it.isSuccessful }
            .flatMap{
                val body = it.body()

                if(body != null){
                    Maybe.just(body.isCodeSent)
                } else{
                    Maybe.empty()
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getCodeLength(): Maybe<Int> {
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

    /**
     * Requests server to verify phone number by the specified verification code.
     * Operates by default on an IoScheduler.
     * @param phoneNumber - phone number which user want to confirm.
     * @param verificationCode - the code that was received in an sms by a user.
     * @return the new Maybe that emits verification result.
     * If error occurred during communication Maybe emits nothing and then just completes
     */
    override fun verifyPhoneNumber(phoneNumber: String, verificationCode: Int): Maybe<PhoneNumberVerificationResult>{
        if(BuildConfig.IgnoreRestRequests){
            return Maybe.just(
                PhoneNumberVerificationResult(
                    isPhoneNumberConfirmed = true,
                    errorMessage = null,
                    token = "1a2b3c4d"
                )
            )
        }

        val verifyPhoneNumberRequestBody = VerifyPhoneNumberRequestBody(phoneNumber, verificationCode)
        val response = authorizationApi.verifyPhoneNumber(verifyPhoneNumberRequestBody)

        return response
            .filter{ it.isSuccessful }
            .flatMap{
                val body = it.body()

                if(body != null){
                    val isPhoneNumberConfirmed = body.isPhoneNumberConfirmed
                    val errorMessage = body.errorMessage
                    val token = body.token
                    val phoneNumberVerificationResult = PhoneNumberVerificationResult(
                        isPhoneNumberConfirmed,
                        errorMessage,
                        token
                    )

                    Maybe.just(phoneNumberVerificationResult)
                } else{
                    Maybe.empty()
                }
            }
            .subscribeOn(ioScheduler)
    }
}