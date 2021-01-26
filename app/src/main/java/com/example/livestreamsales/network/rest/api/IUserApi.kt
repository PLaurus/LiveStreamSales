package com.example.livestreamsales.network.rest.api

import com.example.livestreamsales.model.network.rest.request.UpdateUserInformationRequestBody
import com.example.livestreamsales.model.network.rest.response.GetUserInformationResponseBody
import com.example.livestreamsales.model.network.rest.response.UpdateUserInformationResponseBody
import com.example.livestreamsales.network.rest.api.base.IAuthorizedApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IUserApi: IAuthorizedApi {
    @GET("profile")
    fun getUserInformation(): Single<Response<GetUserInformationResponseBody>>

    @POST("profile")
    fun updateUserInformation(
        @Body updateUserInformationRequestBody: UpdateUserInformationRequestBody
    ): Single<Response<UpdateUserInformationResponseBody>>

    @POST("logout")
    fun logOut(): Single<Response<Unit>>
}