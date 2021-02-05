package tv.wfc.livestreamsales.features.authorizeduser.di.modules.storage

import tv.wfc.livestreamsales.features.authorizeduser.di.modules.storage.qualifiers.LogOutRemoteStorage
import tv.wfc.livestreamsales.features.authorizeduser.di.scope.AuthorizedUserFeatureScope
import tv.wfc.livestreamsales.features.authorizeduser.storage.logout.ILogOutStorage
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {
    @AuthorizedUserFeatureScope
    @Binds
    @LogOutRemoteStorage
    internal abstract fun provideLogOutRemoteStorage(
        logOutRemoteStorage: tv.wfc.livestreamsales.features.authorizeduser.storage.logout.remote.LogOutRemoteStorage
    ): ILogOutStorage
}