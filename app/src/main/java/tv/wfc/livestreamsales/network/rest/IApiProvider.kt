package tv.wfc.livestreamsales.network.rest

import tv.wfc.livestreamsales.network.rest.api.base.IApi

interface IApiProvider {
    fun <T: IApi> createApi(apiClass: Class<T>): T
}