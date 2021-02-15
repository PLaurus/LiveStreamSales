package tv.wfc.livestreamsales.application.di.modules.restapi

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsInformationApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.ILogInApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IProductsInformationApi

@Module
class NotAuthorizedRestApiModule {
    @Provides
    internal fun provideLogInApi(apiProvider: IApiProvider): ILogInApi {
        return apiProvider.createApi(ILogInApi::class.java)
    }

    @Provides
    internal fun provideBroadcastsInformationApi(
        apiProvider: IApiProvider
    ): IBroadcastsInformationApi {
        return apiProvider.createApi(IBroadcastsInformationApi::class.java)
    }

    @Provides
    internal fun provideProductsInformationApi(
        apiProvider: IApiProvider
    ): IProductsInformationApi {
        return apiProvider.createApi(IProductsInformationApi::class.java)
    }
}