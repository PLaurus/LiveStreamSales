package com.example.livestreamsales.di.components.authorizeduser

import com.example.livestreamsales.di.scopes.AuthorizedUserScope
import com.example.livestreamsales.network.rest.interceptors.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named

@Module
class AuthorizedRestModule {

    companion object{
        private const val DEPENDENCY_NAME_AUTHORIZED_RETROFIT = "AUTHORIZED_RETROFIT"
        private const val DEPENDENCY_NAME_AUTHORIZED_OK_HTTP_CLIENT = "AUTHORIZED_OK_HTTP_CLIENT"
    }

    @AuthorizedUserScope
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

    @AuthorizedUserScope
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
        @Named(AuthorizedUserComponent.DEPENDENCY_NAME_AUTHORIZATION_TOKEN)
        authorizationToken: String
    ): AuthorizationInterceptor{
        return AuthorizationInterceptor(authorizationToken)
    }
}