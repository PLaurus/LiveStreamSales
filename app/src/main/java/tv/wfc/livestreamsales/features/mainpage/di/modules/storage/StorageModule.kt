package tv.wfc.livestreamsales.features.mainpage.di.modules.storage

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.mainpage.di.modules.storage.qualifiers.BroadcastsInformationLocalStorage
import tv.wfc.livestreamsales.features.mainpage.di.modules.storage.qualifiers.BroadcastsInformationRemoteStorage
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage

@Module
abstract class StorageModule {
    @Binds
    @BroadcastsInformationRemoteStorage
    internal abstract fun provideBroadcastsInformationRemoteStorage(
        broadcastsInformationRemoteStorage: tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote.BroadcastsInformationRemoteStorage
    ): IBroadcastsInformationStorage

    @Binds
    @BroadcastsInformationLocalStorage
    internal abstract fun provideBroadcastsInformationLocalStorage(
        broadcastsInformationLocalStorage: tv.wfc.livestreamsales.application.storage.broadcastsinformation.local.BroadcastsInformationLocalStorage
    ): IBroadcastsInformationStorage
}