package tv.wfc.livestreamsales.features.rest.interceptors

import okhttp3.Interceptor

interface IAuthorizationInterceptor: Interceptor {
    var token: String?
}