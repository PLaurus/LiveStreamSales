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
import tv.wfc.livestreamsales.application.storage.chat.IChatStorage
import tv.wfc.livestreamsales.application.storage.chat.simulated.SimulatedChatStorage
import tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.ILiveBroadcastingSettingsStorage
import tv.wfc.livestreamsales.application.storage.paymentcardinformation.IPaymentCardInformationStorage
import tv.wfc.livestreamsales.application.storage.products.IProductsStorage
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderStorage
import tv.wfc.livestreamsales.application.storage.userpersonalinformation.IUserPersonalInformationStorage
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsStorage

@Module
abstract class AppStorageModule {
    @Binds
    abstract fun bindAuthorizationRemoteStorage(
        remoteStorage: AuthorizationRemoteStorage
    ): IAuthorizationRemoteStorage

    @Binds
    abstract fun bindAuthorizationLocalStorage(
        localStorage: AuthorizationLocalStorage
    ): IAuthorizationLocalStorage

    @Binds
    @UserInformationRemoteStorage
    internal abstract fun bindUserPersonalInformationRemoteStorage(
        remoteStorage: tv.wfc.livestreamsales.application.storage.userpersonalinformation.remote.UserPersonalInformationRemoteStorage
    ): IUserPersonalInformationStorage

    @Binds
    @UserInformationLocalStorage
    internal abstract fun bindUserInformationLocalStorage(
        localStorage: tv.wfc.livestreamsales.application.storage.userpersonalinformation.local.UserPersonalInformationLocalStorage
    ): IUserPersonalInformationStorage

    @Binds
    @PaymentCardInformationRemoteStorage
    internal abstract fun bindPaymentCardInformationRemoteStorage(
        remoteStorage: tv.wfc.livestreamsales.application.storage.paymentcardinformation.remote.PaymentCardInformationRemoteStorage
    ): IPaymentCardInformationStorage

    @Binds
    @BroadcastAnalyticsRemoteStorage
    internal abstract fun bindBroadcastAnalyticsRemoteStorage(
        remoteStorage: tv.wfc.livestreamsales.features.livebroadcast.storage.remote.BroadcastAnalyticsRemoteStorage
    ): IBroadcastAnalyticsStorage

    @Binds
    @ApplicationSettingsLocalStorage
    internal abstract fun bindApplicationSettingsLocalStorage(
        localStorage: tv.wfc.livestreamsales.application.storage.applicationsettings.local.ApplicationSettingsLocalStorage
    ): IApplicationSettingsStorage

    @Binds
    @BroadcastsInformationRemoteStorage
    internal abstract fun bindBroadcastsInformationRemoteStorage(
        remoteStorage: tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote.BroadcastsRemoteStorage
    ): IBroadcastsStorage

    @Binds
    @BroadcastsInformationLocalStorage
    internal abstract fun bindBroadcastsInformationLocalStorage(
        localStorage: tv.wfc.livestreamsales.application.storage.broadcastsinformation.local.BroadcastsLocalStorage
    ): IBroadcastsStorage

    @Binds
    @ProductsRemoteStorage
    internal abstract fun bindProductsRemoteStorage(
        remoteStorage: tv.wfc.livestreamsales.application.storage.products.remote.ProductsRemoteStorage
    ): IProductsStorage

    @Binds
    @ProductsLocalStorage
    internal abstract fun bindProductsLocalStorage(
        localStorage: tv.wfc.livestreamsales.application.storage.products.local.ProductsLocalStorage
    ): IProductsStorage

    @Binds
    @ProductsOrderRemoteStorage
    internal abstract fun bindProductsOrderRemoteStorage(
        remoteStorage: tv.wfc.livestreamsales.application.storage.productsorder.remote.ProductsOrderRemoteStorage
    ): IProductsOrderStorage

    @Binds
    @ProductsOrderLocalStorage
    internal abstract fun bindProductsOrderLocalStorage(
        localStorage: tv.wfc.livestreamsales.application.storage.productsorder.local.ProductsOrderLocalStorage
    ): IProductsOrderStorage

    @Binds
    internal abstract fun bindChatStorage(
        storage: SimulatedChatStorage
    ): IChatStorage

    @Binds
    @LiveBroadcastingSettingsLocalStorage
    internal abstract fun bindLiveBroadcastingSettingsLocalStorage(
        localStorage: tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.local.LiveBroadcastingSettingsLocalStorage
    ): ILiveBroadcastingSettingsStorage
}