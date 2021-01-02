package com.example.livestreamsales.network.rest

import com.example.livestreamsales.network.rest.api.base.IApi
import com.example.livestreamsales.network.rest.api.base.IAuthorizedApi

interface IRestManager {
    fun <T: IApi> createApi(apiClass: Class<T>): T
    fun <T: IAuthorizedApi> createAuthorizedApi(apiClass: Class<T>): T?
}