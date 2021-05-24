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
import tv.wfc.livestreamsales.application.storage.userpersonalinformation.IUserPersonalInformationStorage
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsStorage

@Module
abstract class AppStorageModule {
    @Binds
    abstract fun bindAuthorizationRemoteStorage(
        authorizationRemoteStorage: AuthorizationRemoteStorage
    ): IAuthorizationRemoteStorage

    @Binds
    abstract fun bindAuthorizationLocalStorage(
        authorizationLocalStorage: AuthorizationLocalStorage
    ): IAuthorizationLocalStorage

    @Binds
    @UserInformationRemoteStorage
    internal abstract fun bindUserPersonalInformationRemoteStorage(
        userPersonalInformationRemoteStorage: tv.wfc.livestreamsales.application.storage.userpersonalinformation.remote.UserPersonalInformationRemoteStorage
    ): IUserPersonalInformationStorage

    @Binds
    @UserInformationLocalStorage
    internal abstract fun bindUserInformationLocalStorage(
        userPersonalInformationLocalStorage: tv.wfc.livestreamsales.application.storage.userpersonalinformation.local.UserPersonalInformationLocalStorage
    ): IUserPersonalInformationStorage

    @Binds
    @BroadcastAnalyticsRemoteStorage
    internal abstract fun bindBroadcastAnalyticsRemoteStorage(
        broadcastAnalyticsRemoteStorage: tv.wfc.livestreamsales.features.livebroadcast.storage.remote.BroadcastAnalyticsRemoteStorage
    ): IBroadcastAnalyticsStorage

    @Binds
    @ApplicationSettingsLocalStorage
    internal abstract fun bindApplicationSettingsLocalStorage(
        applicationSettingsLocalStorage: tv.wfc.livestreamsales.application.storage.applicationsettings.local.ApplicationSettingsLocalStorage
    ): IApplicationSettingsStorage

    @Binds
    @BroadcastsInformationRemoteStorage
    internal abstract fun bindBroadcastsInformationRemoteStorage(
        broadcastsRemoteStorage: tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote.BroadcastsRemoteStorage
    ): IBroadcastsStorage

    @Binds
    @BroadcastsInformationLocalStorage
    internal abstract fun bindBroadcastsInformationLocalStorage(
        broadcastsInformationLocalStorage: tv.wfc.livestreamsales.application.storage.broadcastsinformation.local.BroadcastsLocalStorage
    ): IBroadcastsStorage

    @Binds
    @ProductsRemoteStorage
    internal abstract fun bindProductsRemoteStorage(
        productsInformationRemoteStorage: tv.wfc.livestreamsales.application.storage.products.remote.ProductsRemoteStorage
    ): IProductsStorage

    @Binds
    @ProductsLocalStorage
    internal abstract fun bindProductsLocalStorage(
        productsInformationLocalStorage: tv.wfc.livestreamsales.application.storage.products.local.ProductsLocalStorage
    ): IProductsStorage
}