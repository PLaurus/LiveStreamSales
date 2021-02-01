package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.storage

import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.storage.qualifiers.LogOutRemoteStorage
import tv.wfc.livestreamsales.di.scopes.AuthorizedUserScope
import tv.wfc.livestreamsales.storage.logout.ILogOutStorage
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {
    @AuthorizedUserScope
    @Binds
    @LogOutRemoteStorage
    internal abstract fun provideLogOutRemoteStorage(
        logOutRemoteStorage: tv.wfc.livestreamsales.storage.logout.remote.LogOutRemoteStorage
    ): ILogOutStorage
}