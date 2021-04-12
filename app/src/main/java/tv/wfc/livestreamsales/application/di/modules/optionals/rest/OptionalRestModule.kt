package tv.wfc.livestreamsales.application.di.modules.optionals.rest

import dagger.BindsOptionalOf
import dagger.Module
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import tv.wfc.livestreamsales.application.di.modules.optionals.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.application.di.modules.optionals.rest.qualifiers.AuthorizedOkHttpClient
import tv.wfc.livestreamsales.application.di.modules.optionals.rest.qualifiers.AuthorizedRetrofit
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.interceptors.AuthorizationInterceptor

@Module
abstract class OptionalRestModule {
    @BindsOptionalOf
    @AuthorizedRetrofit
    internal abstract fun optionalAuthorizedRetrofit(): Retrofit

    @BindsOptionalOf
    @AuthorizedOkHttpClient
    internal abstract fun optionalAuthorizedOkHttpClient(): OkHttpClient

    @BindsOptionalOf
    internal abstract fun optionalAuthorizationInterceptor(): AuthorizationInterceptor

    @BindsOptionalOf
    @AuthorizedApiProvider
    internal abstract fun optionalAuthorizedApiProvider(): IApiProvider
}