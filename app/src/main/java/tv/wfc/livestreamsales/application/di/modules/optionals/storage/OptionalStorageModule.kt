package tv.wfc.livestreamsales.application.di.modules.optionals.storage

import dagger.BindsOptionalOf
import dagger.Module
import tv.wfc.livestreamsales.application.di.modules.optionals.storage.qualifiers.BroadcastAnalyticsRemoteStorage
import tv.wfc.livestreamsales.application.di.modules.optionals.storage.qualifiers.LogOutRemoteStorage
import tv.wfc.livestreamsales.application.di.modules.optionals.storage.qualifiers.UserInformationLocalStorage
import tv.wfc.livestreamsales.application.di.modules.optionals.storage.qualifiers.UserInformationRemoteStorage
import tv.wfc.livestreamsales.features.authorizeduser.storage.logout.ILogOutStorage
import tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.IUserInformationStorage
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsStorage

@Module
abstract class OptionalStorageModule {
    @BindsOptionalOf
    @LogOutRemoteStorage
    internal abstract fun optionalLogOutRemoteStorage(): ILogOutStorage

    @BindsOptionalOf
    @UserInformationRemoteStorage
    internal abstract fun optionalUserInformationRemoteStorage(): IUserInformationStorage

    @BindsOptionalOf
    @UserInformationLocalStorage
    internal abstract fun optionalUserInformationLocalStorage(): IUserInformationStorage

    @BindsOptionalOf
    @BroadcastAnalyticsRemoteStorage
    internal abstract fun optionalBroadcastAnalyticsRemoteStorage(): IBroadcastAnalyticsStorage
}