package tv.wfc.livestreamsales.features.rest.interceptors

import tv.wfc.livestreamsales.features.rest.errors.IResponseErrorsManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RestServerErrorsInterceptor @Inject constructor(
    private val responseErrorsManager: IResponseErrorsManager
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if(!response.isSuccessful){
            val statusCode = response.code
            val message = response.message

            responseErrorsManager.checkResponseStatus(statusCode, message)
        }

        return response
    }
}