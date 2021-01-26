package com.example.livestreamsales.di.components.app.modules.authorization

import com.example.livestreamsales.authorization.AuthorizationManager
import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.di.scopes.ApplicationScope
import com.example.livestreamsales.network.rest.IApiProvider
import com.example.livestreamsales.network.rest.api.IAuthorizationApi
import com.example.livestreamsales.repository.authorization.AuthorizationRepository
import com.example.livestreamsales.repository.authorization.IAuthorizationRepository
import com.example.livestreamsales.storage.authorization.local.AuthorizationLocalStorage
import com.example.livestreamsales.storage.authorization.local.IAuthorizationLocalStorage
import com.example.livestreamsales.storage.authorization.remote.AuthorizationRemoteStorage
import com.example.livestreamsales.storage.authorization.remote.IAuthorizationRemoteStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AuthorizationModule {

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationManager(
        authorizationManager: AuthorizationManager
    ): IAuthorizationManager

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationRemoteStorage(
        authorizationRemoteStorage: AuthorizationRemoteStorage
    ): IAuthorizationRemoteStorage

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationLocalStorage(
        authorizationLocalStorage: AuthorizationLocalStorage
    ): IAuthorizationLocalStorage

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationRepository(
        authorizationRepository: AuthorizationRepository
    ): IAuthorizationRepository

    companion object{
        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideAuthorizationApi(apiProvider: IApiProvider): IAuthorizationApi{
            return apiProvider.createApi(IAuthorizationApi::class.java)
        }
    }
}