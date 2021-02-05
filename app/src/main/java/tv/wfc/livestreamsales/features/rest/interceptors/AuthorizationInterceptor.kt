package tv.wfc.livestreamsales.features.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
        private val token: String
): Interceptor {
    companion object{
        private const val HEADER_NAME_AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
                .header(HEADER_NAME_AUTHORIZATION, token)
                .build()

        return chain.proceed(modifiedRequest)
    }
}