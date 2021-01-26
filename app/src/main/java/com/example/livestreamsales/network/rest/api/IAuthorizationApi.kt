package com.example.livestreamsales.network.rest.api

import com.example.livestreamsales.model.network.rest.request.SendVerificationCodeRequestRequestBody
import com.example.livestreamsales.model.network.rest.request.VerifyPhoneNumberRequestBody
import com.example.livestreamsales.model.network.rest.response.GetNextCodeRequestRequiredWaitingTimeResponseBody
import com.example.livestreamsales.model.network.rest.response.GetVerificationCodeLengthResponseBody
import com.example.livestreamsales.model.network.rest.response.SendCodeResponseBody
import com.example.livestreamsales.model.network.rest.response.VerifyPhoneNumberResponseBody
import com.example.livestreamsales.network.rest.api.base.IApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IAuthorizationApi: IApi {
    @POST("login")
    fun sendVerificationCodeRequest(
        @Body sendVerificationCodeRequestRequestBody: SendVerificationCodeRequestRequestBody
    ): Single<Response<SendCodeResponseBody>>

    @POST("sms")
    fun verifyPhoneNumber(
        @Body verifyPhoneNumberRequestBody: VerifyPhoneNumberRequestBody
    ): Single<Response<VerifyPhoneNumberResponseBody>>

    @GET("verification_code_length")
    fun getVerificationCodeLength(): Single<Response<GetVerificationCodeLengthResponseBody>>

    @GET("next_code_request_required_waiting_time")
    fun getNextCodeRequestRequiredWaitingTime(): Single<Response<GetNextCodeRequestRequiredWaitingTimeResponseBody>>
}