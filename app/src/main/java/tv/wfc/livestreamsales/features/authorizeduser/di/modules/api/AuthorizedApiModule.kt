package tv.wfc.livestreamsales.features.authorizeduser.di.modules.api

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.application.di.modules.optionals.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.authorized.IBroadcastAnalyticsApi
import tv.wfc.livestreamsales.features.rest.api.authorized.ILogOutApi
import tv.wfc.livestreamsales.features.rest.api.authorized.IUserInformationApi

@Module
class AuthorizedApiModule {
    @Provides
    internal fun provideLogOutApi(
        @AuthorizedApiProvider
        authorizedApiProvider: IApiProvider
    ): ILogOutApi {
        return authorizedApiProvider.createApi(ILogOutApi::class.java)
    }

    @Provides
    internal fun provideUserInformationApi(
        @AuthorizedApiProvider
        authorizedApiProvider: IApiProvider
    ): IUserInformationApi {
        return authorizedApiProvider.createApi(IUserInformationApi::class.java)
    }

    @Provides
    internal fun provideBroadcastAnalyticsApi(
        @AuthorizedApiProvider
        authorizedApiProvider: IApiProvider
    ): IBroadcastAnalyticsApi {
        return authorizedApiProvider.createApi(IBroadcastAnalyticsApi::class.java)
    }
}