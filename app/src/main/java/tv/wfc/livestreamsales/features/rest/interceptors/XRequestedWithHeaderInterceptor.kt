package tv.wfc.livestreamsales.features.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import tv.wfc.livestreamsales.application.di.modules.rest.qualifiers.XRequestedWithHeaderName
import tv.wfc.livestreamsales.application.di.modules.rest.qualifiers.XRequestedWithHeaderValue
import javax.inject.Inject

class XRequestedWithHeaderInterceptor @Inject constructor(
    @XRequestedWithHeaderName
    private val xRequestedWithHeaderName: String,
    @XRequestedWithHeaderValue
    private val xRequestedWithHeaderValue: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
            .header(xRequestedWithHeaderName, xRequestedWithHeaderValue)
            .build()

        return chain.proceed(modifiedRequest)
    }
}