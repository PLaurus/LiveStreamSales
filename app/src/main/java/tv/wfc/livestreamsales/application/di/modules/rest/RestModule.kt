package tv.wfc.livestreamsales.application.di.modules.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.RetrofitApiProvider
import tv.wfc.livestreamsales.features.rest.errors.IResponseErrorsManager
import tv.wfc.livestreamsales.features.rest.errors.ResponseErrorsManager
import tv.wfc.livestreamsales.features.rest.interceptors.RestServerErrorsInterceptor
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
import tv.wfc.livestreamsales.application.di.modules.rest.qualifiers.*
import tv.wfc.livestreamsales.application.tools.gson.typeadapters.DateTimeTypeAdapter
import javax.net.ssl.HostnameVerifier

@Module
abstract class RestModule {

    companion object{

        @ApplicationScope
        @Provides
        @JvmStatic
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
        @GsonConverterFactory
        @JvmStatic
        fun provideGsonConverterFactory(
            @RestGson
            gson: Gson
        ): Converter.Factory {
            return retrofit2.converter.gson.GsonConverterFactory.create(gson)
        }

        @Provides
        @ScalarsConverterFactory
        @JvmStatic
        internal fun provideScalarsConverterFactory(): Converter.Factory{
            return retrofit2.converter.scalars.ScalarsConverterFactory.create()
        }

        @Provides
        @RestGson
        @JvmStatic
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
        @JvmStatic
        internal fun provideRestDateTimeTypeAdapter(
            @RestDateTimeFormatter
            restDateTimeFormatter: DateTimeFormatter
        ): TypeAdapter<DateTime>{
            return DateTimeTypeAdapter(restDateTimeFormatter)
        }

        @Provides
        @RestDateTimeFormatter
        @JvmStatic
        internal fun provideRestDateTimeFormatter(
            @RestDateTimeFormatPattern
            restDateTimeFormatPattern: String
        ): DateTimeFormatter{
            return DateTimeFormat.forPattern(restDateTimeFormatPattern)
        }

        @Provides
        @RestDateTimeFormatPattern
        @JvmStatic
        internal fun provideRestDateTimeFormatPattern(): String = "yyyy-MM-dd HH:mm:ss Z"

        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideCallAdapterFactory(
            @IoScheduler
            scheduler: Scheduler
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

        @Provides
        @JvmStatic
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
}