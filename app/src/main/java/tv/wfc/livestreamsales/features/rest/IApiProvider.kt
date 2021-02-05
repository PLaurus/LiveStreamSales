package tv.wfc.livestreamsales.features.rest

import tv.wfc.livestreamsales.features.rest.api.base.IApi

interface IApiProvider {
    fun <T: IApi> createApi(apiClass: Class<T>): T
}