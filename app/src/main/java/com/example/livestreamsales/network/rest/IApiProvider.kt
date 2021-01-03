package com.example.livestreamsales.network.rest

import com.example.livestreamsales.network.rest.api.base.IApi

interface IApiProvider {
    fun <T: IApi> createApi(apiClass: Class<T>): T
}