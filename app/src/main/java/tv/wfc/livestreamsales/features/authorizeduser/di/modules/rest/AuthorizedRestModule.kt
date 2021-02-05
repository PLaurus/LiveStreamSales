package tv.wfc.livestreamsales.features.authorizeduser.di.modules.rest

import tv.wfc.livestreamsales.features.authorizeduser.di.modules.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.features.authorizeduser.di.scope.AuthorizedUserFeatureScope
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.RetrofitApiProvider
import tv.wfc.livestreamsales.features.rest.interceptors.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import tv.wfc.livestreamsales.features.authorizeduser.di.qualifiers.AuthorizationToken
import javax.inject.Named

@Module
class AuthorizedRestModule {
    companion object{
        private const val DEPENDENCY_NAME_AUTHORIZED_OK_HTTP_CLIENT = "AUTHORIZED_OK_HTTP_CLIENT"
        private const val DEPENDENCY_NAME_AUTHORIZED_RETROFIT = "AUTHORIZED_RETROFIT"
    }

    @AuthorizedUserFeatureScope
    @Provides
    @Named(DEPENDENCY_NAME_AUTHORIZED_RETROFIT)
    fun provideAuthorizedRetrofit(
        baseRetrofit: Retrofit,
        @Named(DEPENDENCY_NAME_AUTHORIZED_OK_HTTP_CLIENT)
        authorizedOkHttpClient: OkHttpClient
    ): Retrofit{
        return baseRetrofit.newBuilder()
            .client(authorizedOkHttpClient)
            .build()
    }

    @AuthorizedUserFeatureScope
    @Provides
    @Named(DEPENDENCY_NAME_AUTHORIZED_OK_HTTP_CLIENT)
    fun provideAuthorizedOkHttpClient(
        baseOkHttpClient: OkHttpClient,
        authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient{
        return baseOkHttpClient.newBuilder()
            .addInterceptor(authorizationInterceptor)
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
        @Named(DEPENDENCY_NAME_AUTHORIZED_RETROFIT)
        authorizedRetrofit: Retrofit
    ): IApiProvider {
        return RetrofitApiProvider(authorizedRetrofit)
    }
}