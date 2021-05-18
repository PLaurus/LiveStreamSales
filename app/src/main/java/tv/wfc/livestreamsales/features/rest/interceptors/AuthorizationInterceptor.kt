package tv.wfc.livestreamsales.features.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(): IAuthorizationInterceptor {
    companion object{
        private const val HEADER_NAME_AUTHORIZATION = "Authorization"
    }

    override var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = this.token
        val originalRequest = chain.request()

        if(token == null) return chain.proceed(originalRequest)

        val modifiedRequest = originalRequest.newBuilder()
                .header(HEADER_NAME_AUTHORIZATION, "Bearer $token")
                .build()

        return chain.proceed(modifiedRequest)
    }
}