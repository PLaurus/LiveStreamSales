package com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.api

import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.rest.qualifiers.AuthorizedApiProvider
import com.example.livestreamsales.network.rest.IApiProvider
import com.example.livestreamsales.network.rest.api.authorized.ILogOutApi
import dagger.Module
import dagger.Provides

@Module
class ApiModule {
    @Provides
    internal fun provideLogOutApi(
        @AuthorizedApiProvider
        authorizedApiProvider: IApiProvider
    ): ILogOutApi{
        return authorizedApiProvider.createApi(ILogOutApi::class.java)
    }
}