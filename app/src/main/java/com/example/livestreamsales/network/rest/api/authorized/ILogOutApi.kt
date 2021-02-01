package com.example.livestreamsales.network.rest.api.authorized

import com.example.livestreamsales.network.rest.api.base.IAuthorizedApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.POST

interface ILogOutApi: IAuthorizedApi {
    @POST("logout")
    fun logOut(): Single<Response<Unit>>
}