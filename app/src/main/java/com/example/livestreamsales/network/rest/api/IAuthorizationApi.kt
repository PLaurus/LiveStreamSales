package com.example.livestreamsales.network.rest.api

import com.example.livestreamsales.model.network.rest.request.ConfirmTelephoneNumberRequestBody
import com.example.livestreamsales.model.network.rest.request.SendVerificationCodeRequestRequestBody
import com.example.livestreamsales.model.network.rest.response.ConfirmTelephoneNumberResponseBody
import com.example.livestreamsales.model.network.rest.response.SendCodeResponseBody
import com.example.livestreamsales.network.rest.api.base.IApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthorizationApi: IApi {

    @POST("login")
    fun sendVerificationCodeRequest(
        @Body sendVerificationCodeRequestRequestBody: SendVerificationCodeRequestRequestBody
    ): Single<Response<SendCodeResponseBody>>

    @POST("confirm_telephone_number")
    fun confirmTelephoneNumber(
        @Body confirmTelephoneNumberRequestBody: ConfirmTelephoneNumberRequestBody
    ): Single<Response<ConfirmTelephoneNumberResponseBody>>
}