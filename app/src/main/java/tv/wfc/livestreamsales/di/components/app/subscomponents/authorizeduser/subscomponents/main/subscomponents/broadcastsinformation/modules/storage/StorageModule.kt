package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.storage

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.storage.qualifiers.BroadcastsInformationLocalStorage
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.storage.qualifiers.BroadcastsInformationRemoteStorage
import tv.wfc.livestreamsales.storage.broadcastsinformation.IBroadcastsInformationStorage

@Module
abstract class StorageModule {
    @Binds
    @BroadcastsInformationRemoteStorage
    internal abstract fun provideBroadcastsInformationRemoteStorage(
        broadcastsInformationRemoteStorage: tv.wfc.livestreamsales.storage.broadcastsinformation.remote.BroadcastsInformationRemoteStorage
    ): IBroadcastsInformationStorage

    @Binds
    @BroadcastsInformationLocalStorage
    internal abstract fun provideBroadcastsInformationLocalStorage(
        broadcastsInformationLocalStorage: tv.wfc.livestreamsales.storage.broadcastsinformation.local.BroadcastsInformationLocalStorage
    ): IBroadcastsInformationStorage
}