package tv.wfc.livestreamsales.application.di.modules.storage

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ApplicationSettingsLocalStorage
import tv.wfc.livestreamsales.application.storage.applicationsettings.IApplicationSettingsStorage
import tv.wfc.livestreamsales.application.storage.authorization.local.AuthorizationLocalStorage
import tv.wfc.livestreamsales.application.storage.authorization.local.IAuthorizationLocalStorage
import tv.wfc.livestreamsales.application.storage.authorization.remote.AuthorizationRemoteStorage
import tv.wfc.livestreamsales.application.storage.authorization.remote.IAuthorizationRemoteStorage
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.BroadcastsInformationLocalStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.BroadcastsInformationRemoteStorage

@Module
abstract class AppStorageModule {
    @Binds
    abstract fun provideAuthorizationRemoteStorage(
        authorizationRemoteStorage: AuthorizationRemoteStorage
    ): IAuthorizationRemoteStorage

    @Binds
    abstract fun provideAuthorizationLocalStorage(
        authorizationLocalStorage: AuthorizationLocalStorage
    ): IAuthorizationLocalStorage

    @Binds
    @ApplicationSettingsLocalStorage
    internal abstract fun provideApplicationSettingsLocalStorage(
        applicationSettingsLocalStorage: tv.wfc.livestreamsales.application.storage.applicationsettings.local.ApplicationSettingsLocalStorage
    ): IApplicationSettingsStorage

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