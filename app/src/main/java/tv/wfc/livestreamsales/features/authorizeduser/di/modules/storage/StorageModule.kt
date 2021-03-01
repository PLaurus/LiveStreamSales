package tv.wfc.livestreamsales.features.authorizeduser.di.modules.storage

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.storage.qualifiers.LogOutRemoteStorage
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.storage.qualifiers.UserInformationLocalStorage
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.storage.qualifiers.UserInformationRemoteStorage
import tv.wfc.livestreamsales.features.authorizeduser.di.scope.AuthorizedUserFeatureScope
import tv.wfc.livestreamsales.features.authorizeduser.storage.logout.ILogOutStorage
import tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.IUserInformationStorage

@Module
abstract class StorageModule {
    @AuthorizedUserFeatureScope
    @Binds
    @LogOutRemoteStorage
    internal abstract fun provideLogOutRemoteStorage(
        logOutRemoteStorage: tv.wfc.livestreamsales.features.authorizeduser.storage.logout.remote.LogOutRemoteStorage
    ): ILogOutStorage

    @AuthorizedUserFeatureScope
    @Binds
    @UserInformationRemoteStorage
    internal abstract fun provideUserInformationRemoteStorage(
        userInformationInformationRemoteStorage: tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.remote.UserInformationRemoteStorage
    ): IUserInformationStorage

    @AuthorizedUserFeatureScope
    @Binds
    @UserInformationLocalStorage
    internal abstract fun provideUserInformationLocalStorage(
        userInformationInformationLocalStorage: tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.local.UserInformationLocalStorage
    ): IUserInformationStorage
}