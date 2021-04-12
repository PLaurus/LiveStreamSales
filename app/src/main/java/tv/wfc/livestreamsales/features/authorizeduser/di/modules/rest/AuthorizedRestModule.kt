package tv.wfc.livestreamsales.features.authorizeduser.di.modules.rest

import tv.wfc.livestreamsales.application.di.modules.optionals.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.features.authorizeduser.di.scope.AuthorizedUserFeatureScope
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.RetrofitApiProvider
import tv.wfc.livestreamsales.features.rest.interceptors.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import tv.wfc.livestreamsales.application.di.modules.optionals.rest.qualifiers.AuthorizedOkHttpClient
import tv.wfc.livestreamsales.application.di.modules.optionals.rest.qualifiers.AuthorizedRetrofit
import tv.wfc.livestreamsales.features.authorizeduser.di.qualifiers.AuthorizationToken
import javax.inject.Named

@Module
class AuthorizedRestModule {
    @AuthorizedUserFeatureScope
    @Provides
    @AuthorizedRetrofit
    fun provideAuthorizedRetrofit(
        baseRetrofit: Retrofit,
        @AuthorizedOkHttpClient
        authorizedOkHttpClient: OkHttpClient
    ): Retrofit{
        return baseRetrofit.newBuilder()
            .client(authorizedOkHttpClient)
            .build()
    }

    @AuthorizedUserFeatureScope
    @Provides
    @AuthorizedOkHttpClient
    fun provideAuthorizedOkHttpClient(
        baseOkHttpClient: OkHttpClient,
        authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient{
        return baseOkHttpClient.newBuilder()
            .apply{
                interceptors().add(0, authorizationInterceptor)
            }
            .build()
    }

    @Provides
    fun provideAuthorizationInterceptor(
        @AuthorizationToken
        authorizationToken: String
    ): AuthorizationInterceptor {
        return AuthorizationInterceptor(authorizationToken)
    }

    @AuthorizedUserFeatureScope
    @Provides
    @AuthorizedApiProvider
    fun provideAuthorizedApiProvider(
        @AuthorizedRetrofit
        authorizedRetrofit: Retrofit
    ): IApiProvider {
        return RetrofitApiProvider(authorizedRetrofit)
    }
}