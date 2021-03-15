package tv.wfc.livestreamsales.application.di.modules.storage

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.*
import tv.wfc.livestreamsales.application.storage.applicationsettings.IApplicationSettingsStorage
import tv.wfc.livestreamsales.application.storage.authorization.local.AuthorizationLocalStorage
import tv.wfc.livestreamsales.application.storage.authorization.local.IAuthorizationLocalStorage
import tv.wfc.livestreamsales.application.storage.authorization.remote.AuthorizationRemoteStorage
import tv.wfc.livestreamsales.application.storage.authorization.remote.IAuthorizationRemoteStorage
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsStorage
import tv.wfc.livestreamsales.application.storage.products.IProductsStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsLocalStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsRemoteStorage

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
        broadcastsRemoteStorage: tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote.BroadcastsRemoteStorage
    ): IBroadcastsStorage

    @Binds
    @BroadcastsInformationLocalStorage
    internal abstract fun provideBroadcastsInformationLocalStorage(
        broadcastsInformationLocalStorage: tv.wfc.livestreamsales.application.storage.broadcastsinformation.local.BroadcastsLocalStorage
    ): IBroadcastsStorage

    @Binds
    @ProductsRemoteStorage
    internal abstract fun provideProductsRemoteStorage(
        productsInformationRemoteStorage: tv.wfc.livestreamsales.application.storage.products.remote.ProductsRemoteStorage
    ): IProductsStorage

    @Binds
    @ProductsLocalStorage
    internal abstract fun provideProductsLocalStorage(
        productsInformationLocalStorage: tv.wfc.livestreamsales.application.storage.products.local.ProductsLocalStorage
    ): IProductsStorage
}