package tv.wfc.livestreamsales.features.mainpage.di.modules.api

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsInformationApi

@Module
class ApiModule {
    @Provides
    internal fun provideBroadcastsInformationApi(
        apiProvider: IApiProvider
    ): IBroadcastsInformationApi {
        return apiProvider.createApi(IBroadcastsInformationApi::class.java)
    }
}