package com.example.livestreamsales.network.rest.api

import com.example.livestreamsales.model.request.SendCodeRequestBody
import com.example.livestreamsales.model.response.SendCodeResponseBody
import com.example.livestreamsales.network.rest.api.base.IApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthorizationApi: IApi {

    @POST("send_code")
    fun sendCode(
        @Body sendCodeRequestBody: SendCodeRequestBody
    ): Single<Response<SendCodeResponseBody>>
}