package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.api

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.network.rest.IApiProvider
import tv.wfc.livestreamsales.network.rest.api.notauthorized.IBroadcastsInformationApi

@Module
class ApiModule {
    @Provides
    internal fun provideBroadcastsInformationApi(
        apiProvider: IApiProvider
    ): IBroadcastsInformationApi{
        return apiProvider.createApi(IBroadcastsInformationApi::class.java)
    }
}