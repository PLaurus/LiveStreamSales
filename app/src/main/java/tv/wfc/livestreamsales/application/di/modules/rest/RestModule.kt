package tv.wfc.livestreamsales.application.di.modules.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.rest.qualifiers.*
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.RetrofitApiProvider
import tv.wfc.livestreamsales.features.rest.errors.IResponseErrorsManager
import tv.wfc.livestreamsales.features.rest.errors.ResponseErrorsManager
import tv.wfc.livestreamsales.features.rest.interceptors.AuthorizationInterceptor
import tv.wfc.livestreamsales.features.rest.interceptors.IAuthorizationInterceptor
import javax.net.ssl.HostnameVerifier

@Module
abstract class RestModule {
    companion object{
        @ApplicationScope
        @Provides
        fun provideBaseRetrofit(
            @RestBaseUrl
            baseUrl: String,
            @ScalarsConverterFactory
            scalarsConverterFactory: Converter.Factory,
            @GsonConverterFactory
            gsonConverterFactory: Converter.Factory,
            callAdapterFactory: CallAdapter.Factory,
            okHttpClient: OkHttpClient
        ): Retrofit{
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(scalarsConverterFactory)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .client(okHttpClient)
                .build()
        }

        @ApplicationScope
        @Provides
        @RestBaseUrl
        fun provideRestBaseUrl(): String = "https://stream-api.mywfc.ru/api/"

        @ApplicationScope
        @Provides
        fun provideOkHttpClient(
            handshakeCertificates: HandshakeCertificates,
            hostnameVerifier: HostnameVerifier,
            authorizationInterceptor: IAuthorizationInterceptor,
            @XRequestedWithHeaderInterceptor
            xRequestedWithHeaderInterceptor: Interceptor,
            @HttpLoggingInterceptor
            httpLoggingInterceptor: Interceptor,
            @RestServerErrorsInterceptor
            restServerErrorsInterceptor: Interceptor
        ): OkHttpClient{
            return OkHttpClient.Builder()
                .sslSocketFactory(
                    handshakeCertificates.sslSocketFactory(),
                    handshakeCertificates.trustManager
                )
                .hostnameVerifier(hostnameVerifier)
                .addInterceptor(authorizationInterceptor)
                .addInterceptor(xRequestedWithHeaderInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(restServerErrorsInterceptor)
                .build()
        }

        @ApplicationScope
        @Provides
        @GsonConverterFactory
        internal fun provideGsonConverterFactory(
            @RestGson
            gson: Gson
        ): Converter.Factory {
            return retrofit2.converter.gson.GsonConverterFactory.create(gson)
        }

        @Provides
        @ScalarsConverterFactory
        internal fun provideScalarsConverterFactory(): Converter.Factory{
            return retrofit2.converter.scalars.ScalarsConverterFactory.create()
        }

        @Provides
        @RestGson
        internal fun provideGson(): Gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        @ApplicationScope
        @Provides
        internal fun provideCallAdapterFactory(
            @IoScheduler
            scheduler: Scheduler
        ): CallAdapter.Factory{
            return RxJava3CallAdapterFactory.createWithScheduler(scheduler)
        }

        @Provides
        @HttpLoggingInterceptor
        internal fun provideHttpLoggingInterceptor(): Interceptor {
            return okhttp3.logging.HttpLoggingInterceptor().apply {
                level = if(BuildConfig.DEBUG){
                    okhttp3.logging.HttpLoggingInterceptor.Level.BODY
                } else {
                    okhttp3.logging.HttpLoggingInterceptor.Level.NONE
                }
            }
        }

        @Provides
        @AuthorizationHeaderName
        internal fun provideAuthorizationHeaderName() = "Authorization"

        @Provides
        @XRequestedWithHeaderName
        internal fun provideXRequestedWithHeaderName() = "X-Requested-With"

        @Provides
        @XRequestedWithHeaderValue
        internal fun provideXRequestedWithHeaderValue() = "XMLHttpRequest"
    }

    @ApplicationScope
    @Binds
    abstract fun provideApiProvider(
        retrofitApiProvider: RetrofitApiProvider
    ): IApiProvider

    @ApplicationScope
    @Binds
    abstract fun provideResponseErrorsManager(
        responseErrorsManager: ResponseErrorsManager
    ): IResponseErrorsManager

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationInterceptor(
        interceptor: AuthorizationInterceptor
    ): IAuthorizationInterceptor

    @Binds
    @XRequestedWithHeaderInterceptor
    abstract fun bindXRequestedWithHeaderInterceptor(
        interceptor: tv.wfc.livestreamsales.features.rest.interceptors.XRequestedWithHeaderInterceptor
    ): Interceptor

    @Binds
    @RestServerErrorsInterceptor
    abstract fun bindRestServerErrorsInterceptor(
        interceptor: tv.wfc.livestreamsales.features.rest.interceptors.RestServerErrorsInterceptor
    ): Interceptor
}