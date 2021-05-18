package tv.wfc.livestreamsales.application.di.modules.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.rest.qualifiers.*
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.application.tools.gson.typeadapters.DateTimeTypeAdapter
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.RetrofitApiProvider
import tv.wfc.livestreamsales.features.rest.errors.IResponseErrorsManager
import tv.wfc.livestreamsales.features.rest.errors.ResponseErrorsManager
import tv.wfc.livestreamsales.features.rest.interceptors.AuthorizationInterceptor
import tv.wfc.livestreamsales.features.rest.interceptors.IAuthorizationInterceptor
import tv.wfc.livestreamsales.features.rest.interceptors.RestServerErrorsInterceptor
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
            httpLoggingInterceptor: HttpLoggingInterceptor,
            restServerErrorsInterceptor: RestServerErrorsInterceptor
        ): OkHttpClient{
            return OkHttpClient.Builder()
                .sslSocketFactory(
                    handshakeCertificates.sslSocketFactory(),
                    handshakeCertificates.trustManager
                )
                .hostnameVerifier(hostnameVerifier)
                .addInterceptor(authorizationInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(restServerErrorsInterceptor)
                .build()
        }

        @ApplicationScope
        @Provides
        @GsonConverterFactory
        fun provideGsonConverterFactory(
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
        internal fun provideGson(
            @RestDateTimeTypeAdapter
            restDateTimeAdapter: TypeAdapter<DateTime>
        ): Gson {
            return GsonBuilder()
                .registerTypeAdapter(DateTime::class.java, restDateTimeAdapter)
                .setPrettyPrinting()
                .create()
        }

        @Provides
        @RestDateTimeTypeAdapter
        internal fun provideRestDateTimeTypeAdapter(
            @RestDateTimeFormatter
            restDateTimeFormatter: DateTimeFormatter
        ): TypeAdapter<DateTime>{
            return DateTimeTypeAdapter(restDateTimeFormatter)
        }

        @Provides
        @RestDateTimeFormatter
        internal fun provideRestDateTimeFormatter(
            @RestDateTimeFormatPattern
            restDateTimeFormatPattern: String
        ): DateTimeFormatter{
            return DateTimeFormat.forPattern(restDateTimeFormatPattern)
        }

        @Provides
        @RestDateTimeFormatPattern
        internal fun provideRestDateTimeFormatPattern(): String = "yyyy-MM-dd'T'HH:mm:ssZ"

        @ApplicationScope
        @Provides
        fun provideCallAdapterFactory(
            @IoScheduler
            scheduler: Scheduler
        ): CallAdapter.Factory{
            return RxJava3CallAdapterFactory.createWithScheduler(scheduler)
        }

        @ApplicationScope
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply{
                level = if(BuildConfig.DEBUG){
                    HttpLoggingInterceptor.Level.BODY
                } else{
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        }

        @Provides
        fun provideRestServerErrorInterceptor(
            responseErrorsManager: IResponseErrorsManager
        ): RestServerErrorsInterceptor {
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
    abstract fun provideResponseErrorsManager(
        responseErrorsManager: ResponseErrorsManager
    ): IResponseErrorsManager

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationInterceptor(
        authorizationInterceptor: AuthorizationInterceptor
    ): IAuthorizationInterceptor
}