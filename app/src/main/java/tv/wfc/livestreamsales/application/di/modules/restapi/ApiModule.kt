package tv.wfc.livestreamsales.application.di.modules.restapi

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.authorized.IBroadcastAnalyticsApi
import tv.wfc.livestreamsales.features.rest.api.authorized.IPaymentCardInformationApi
import tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.IProductsOrdersApi
import tv.wfc.livestreamsales.features.rest.api.authorized.IUserPersonalInformationApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IAuthorizationApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsApi
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IProductsApi
import tv.wfc.livestreamsales.features.rest.api.streamchat.IStreamChatApi

@Module
class ApiModule {
    @Provides
    internal fun provideAuthorizationApi(apiProvider: IApiProvider): IAuthorizationApi {
        return apiProvider.createApi(IAuthorizationApi::class.java)
    }

    @Provides
    internal fun provideUserPersonalInformationApi(
        apiProvider: IApiProvider
    ): IUserPersonalInformationApi {
        return apiProvider.createApi(IUserPersonalInformationApi::class.java)
    }

    @Provides
    internal fun providePaymentCardInformationApi(
        apiProvider: IApiProvider
    ): IPaymentCardInformationApi{
        return apiProvider.createApi(IPaymentCardInformationApi::class.java)
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

    @Provides
    internal fun provideProductsOrderApi(
        apiProvider: IApiProvider
    ): IProductsOrdersApi {
        return apiProvider.createApi(IProductsOrdersApi::class.java)
    }

    @Provides
    internal fun provideIStreamChatApi(
        apiProvider: IApiProvider
    ): IStreamChatApi {
        return apiProvider.createApi(IStreamChatApi::class.java)
    }
}