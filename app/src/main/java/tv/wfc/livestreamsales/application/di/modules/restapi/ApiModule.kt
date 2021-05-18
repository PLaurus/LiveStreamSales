package tv.wfc.livestreamsales.application.di.modules.restapi

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.authorized.IBroadcastAnalyticsApi
import tv.wfc.livestreamsales.features.rest.api.authorized.IUserInformationApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IAuthorizationApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IProductsApi

@Module
class ApiModule {
    @Provides
    internal fun provideAuthorizationApi(apiProvider: IApiProvider): IAuthorizationApi {
        return apiProvider.createApi(IAuthorizationApi::class.java)
    }

    @Provides
    internal fun provideUserInformationApi(
        apiProvider: IApiProvider
    ): IUserInformationApi {
        return apiProvider.createApi(IUserInformationApi::class.java)
    }

    @Provides
    internal fun provideBroadcastAnalyticsApi(
        apiProvider: IApiProvider
    ): IBroadcastAnalyticsApi {
        return apiProvider.createApi(IBroadcastAnalyticsApi::class.java)
    }

    @Provides
    internal fun provideBroadcastsInformationApi(
        apiProvider: IApiProvider
    ): IBroadcastsApi {
        return apiProvider.createApi(IBroadcastsApi::class.java)
    }

    @Provides
    internal fun provideProductsInformationApi(
        apiProvider: IApiProvider
    ): IProductsApi {
        return apiProvider.createApi(IProductsApi::class.java)
    }
}