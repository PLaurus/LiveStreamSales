package com.example.livestreamsales.di.components.authorizeduser.modules.rest

import com.example.livestreamsales.di.components.authorizeduser.AuthorizedUserComponent
import com.example.livestreamsales.di.components.authorizeduser.modules.rest.qualifiers.AuthorizedApiProvider
import com.example.livestreamsales.di.scopes.AuthorizedUserScope
import com.example.livestreamsales.network.rest.IApiProvider
import com.example.livestreamsales.network.rest.RetrofitApiProvider
import com.example.livestreamsales.network.rest.interceptors.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named

@Module
class AuthorizedRestModule {

    companion object{
        private const val DEPENDENCY_NAME_AUTHORIZED_OK_HTTP_CLIENT = "AUTHORIZED_OK_HTTP_CLIENT"
        private const val DEPENDENCY_NAME_AUTHORIZED_RETROFIT = "AUTHORIZED_RETROFIT"
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

    @AuthorizedUserScope
    @Provides
    @AuthorizedApiProvider
    fun provideAuthorizedApiProvider(
        @Named(DEPENDENCY_NAME_AUTHORIZED_RETROFIT)
        authorizedRetrofit: Retrofit
    ): IApiProvider{
        return RetrofitApiProvider(authorizedRetrofit)
    }
}