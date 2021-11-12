package tv.wfc.livestreamsales.features.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import tv.wfc.livestreamsales.application.di.modules.rest.qualifiers.AuthorizationHeaderName
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    @AuthorizationHeaderName
    private val authorizationHeaderName: String
) : IAuthorizationInterceptor {
    override var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = this.token
        val originalRequest = chain.request()

        if (token == null) return chain.proceed(originalRequest)

        val modifiedRequest = originalRequest.newBuilder()
            .header(authorizationHeaderName, "Bearer $token")
            .build()

        return chain.proceed(modifiedRequest)
    }
}