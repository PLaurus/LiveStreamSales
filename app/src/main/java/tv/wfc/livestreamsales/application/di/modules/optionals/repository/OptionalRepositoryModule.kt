package tv.wfc.livestreamsales.application.di.modules.optionals.repository

import dagger.BindsOptionalOf
import dagger.Module
import tv.wfc.livestreamsales.features.authorizeduser.repository.logout.ILogOutRepository
import tv.wfc.livestreamsales.features.authorizeduser.repository.userinformation.IUserInformationRepository
import tv.wfc.livestreamsales.features.livebroadcast.repository.IBroadcastAnalyticsRepository

@Module
abstract class OptionalRepositoryModule {
    @BindsOptionalOf
    internal abstract fun optionalLogOutRepository(): ILogOutRepository

    @BindsOptionalOf
    internal abstract fun optionalUserInformationRepository(): IUserInformationRepository

    @BindsOptionalOf
    internal abstract fun optionalBroadcastAnalyticsRepository(): IBroadcastAnalyticsRepository
}