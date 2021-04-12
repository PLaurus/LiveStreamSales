package tv.wfc.livestreamsales.application.di.modules.optionals.api

import dagger.BindsOptionalOf
import dagger.Module
import tv.wfc.livestreamsales.features.rest.api.authorized.IBroadcastAnalyticsApi
import tv.wfc.livestreamsales.features.rest.api.authorized.ILogOutApi
import tv.wfc.livestreamsales.features.rest.api.authorized.IUserInformationApi

@Module
abstract class OptionalApiModule {
    @BindsOptionalOf
    internal abstract fun optionalLogOutApi(): ILogOutApi

    @BindsOptionalOf
    internal abstract fun optionalUserInformationApi(): IUserInformationApi

    @BindsOptionalOf
    internal abstract fun optionalBroadcastAnalyticsApi(): IBroadcastAnalyticsApi
}