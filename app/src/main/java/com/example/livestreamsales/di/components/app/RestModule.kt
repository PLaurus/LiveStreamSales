package com.example.livestreamsales.di.components.app

import com.example.livestreamsales.BuildConfig
import com.example.livestreamsales.di.scopes.ApplicationScope
import com.example.livestreamsales.network.rest.IApiProvider
import com.example.livestreamsales.network.rest.RetrofitApiProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

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
        fun provideRestBaseUrl(): String = "https://www.google.com/"

        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideOkHttpClient(
                httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient{
            return OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
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
    }

    @ApplicationScope
    @Binds
    abstract fun provideRestManager(retrofitApiProvider: RetrofitApiProvider): IApiProvider
}