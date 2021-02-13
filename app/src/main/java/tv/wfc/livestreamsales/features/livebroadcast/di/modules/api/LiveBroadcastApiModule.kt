package tv.wfc.livestreamsales.features.livebroadcast.di.modules.api

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.authorized.IBroadcastAnalyticsApi

@Module
class LiveBroadcastApiModule {
    @Provides
    internal fun provideBroadcastAnalyticsApi(
        @AuthorizedApiProvider
        authorizedApiProvider: IApiProvider
    ): IBroadcastAnalyticsApi {
        return authorizedApiProvider.createApi(IBroadcastAnalyticsApi::class.java)
    }
}