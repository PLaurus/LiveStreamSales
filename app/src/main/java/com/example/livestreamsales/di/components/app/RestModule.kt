package com.example.livestreamsales.di.components.app

import com.example.livestreamsales.BuildConfig
import com.example.livestreamsales.di.scopes.ApplicationScope
import com.example.livestreamsales.network.rest.IApiProvider
import com.example.livestreamsales.network.rest.RetrofitApiProvider
import com.example.livestreamsales.network.rest.errors.IResponseErrorsManager
import com.example.livestreamsales.network.rest.errors.ResponseErrorsManager
import com.example.livestreamsales.network.rest.interceptors.RestServerErrorsInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.net.ssl.HostnameVerifier

@Module
abstract class RestModule {

    companion object{
        private const val DEPENDENCY_NAME_REST_BASE_URL = "REST_BASE_URL"
        private const val DEPENDENCY_NAME_JSON_CONVERTER_FACTORY = "JSON_CONVERTER_FACTORY"

        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideBaseRetrofit(
            @Named(DEPENDENCY_NAME_REST_BASE_URL) baseUrl: String,
            @Named(DEPENDENCY_NAME_JSON_CONVERTER_FACTORY) jsonConverterFactory: Converter.Factory,
            callAdapterFactory: CallAdapter.Factory,
            okHttpClient: OkHttpClient
        ): Retrofit{
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(jsonConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .client(okHttpClient)
                .build()
        }

        @ApplicationScope
        @Provides
        @Named(DEPENDENCY_NAME_REST_BASE_URL)
        @JvmStatic
        fun provideRestBaseUrl(): String = "https://stream-api.mywfc.ru/api/"

        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideBaseOkHttpClient(
            handshakeCertificates: HandshakeCertificates,
            hostnameVerifier: HostnameVerifier,
            httpLoggingInterceptor: HttpLoggingInterceptor,
            restServerErrorsInterceptor: RestServerErrorsInterceptor
        ): OkHttpClient{
            return OkHttpClient.Builder()
                .sslSocketFactory(
                    handshakeCertificates.sslSocketFactory(),
                    handshakeCertificates.trustManager
                )
                .hostnameVerifier(hostnameVerifier)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(restServerErrorsInterceptor)
                .build()
        }

        @ApplicationScope
        @Provides
        @Named(DEPENDENCY_NAME_JSON_CONVERTER_FACTORY)
        @JvmStatic
        fun provideJsonConverterFactory(): Converter.Factory {
            return GsonConverterFactory.create()
        }

        // TODO: provide ScalarsConverterFactory

        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideCallAdapterFactory(
            @Named(ReactiveXModule.DEPENDENCY_NAME_IO_SCHEDULER) scheduler: Scheduler
        ): CallAdapter.Factory{
            return RxJava3CallAdapterFactory.createWithScheduler(scheduler)
        }

        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply{
                level = if(BuildConfig.DEBUG){
                    HttpLoggingInterceptor.Level.BODY
                } else{
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        }

        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideRestServerErrorInterceptor(
            responseErrorsManager: IResponseErrorsManager
        ): RestServerErrorsInterceptor{
            return RestServerErrorsInterceptor(responseErrorsManager)
        }
    }

    @ApplicationScope
    @Binds
    abstract fun provideApiProvider(
        retrofitApiProvider: RetrofitApiProvider
    ): IApiProvider

    @ApplicationScope
    @Binds
    abstract fun provideRestServerErrorsManager(
        restServerErrorsManager: ResponseErrorsManager
    ): IResponseErrorsManager
}