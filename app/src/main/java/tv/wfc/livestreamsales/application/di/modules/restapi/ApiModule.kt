package tv.wfc.livestreamsales.application.di.modules.restapi

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.ILogInApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IProductsApi

@Module
class ApiModule {
    @Provides
    internal fun provideLogInApi(apiProvider: IApiProvider): ILogInApi {
        return apiProvider.createApi(ILogInApi::class.java)
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