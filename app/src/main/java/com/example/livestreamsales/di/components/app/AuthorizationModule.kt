package com.example.livestreamsales.di.components.app

import com.example.livestreamsales.authorization.AuthorizationManager
import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.di.scopes.ApplicationScope
import com.example.livestreamsales.network.rest.IRestManager
import com.example.livestreamsales.network.rest.api.IAuthorizationApi
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AuthorizationModule {

    @Binds
    @ApplicationScope
    abstract fun provideAuthorizationManager(authorizationManager: AuthorizationManager): IAuthorizationManager

    companion object{
        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideAuthorizationApi(restManager: IRestManager): IAuthorizationApi{
            return restManager.createApi(IAuthorizationApi::class.java)
        }
    }
}