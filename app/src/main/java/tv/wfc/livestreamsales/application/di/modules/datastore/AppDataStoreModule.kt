package tv.wfc.livestreamsales.application.di.modules.datastore

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.datastore.streamchatmessage.pusher.IStreamChatMessagePusherDataStore
import tv.wfc.livestreamsales.application.datastore.streamchatmessage.pusher.StreamChatMessagePusherDataStore
import tv.wfc.livestreamsales.application.datastore.streamchatmessage.rest.IStreamChatMessageRestDataStore
import tv.wfc.livestreamsales.application.datastore.streamchatmessage.rest.StreamChatMessageRestDataStore
import tv.wfc.livestreamsales.application.di.modules.datastore.qualifiers.*
import tv.wfc.livestreamsales.application.storage.applicationsettings.IApplicationSettingsDataStore
import tv.wfc.livestreamsales.application.storage.authorization.local.AuthorizationLocalDataStore
import tv.wfc.livestreamsales.application.storage.authorization.local.IAuthorizationLocalDataStore
import tv.wfc.livestreamsales.application.storage.authorization.remote.AuthorizationRemoteDataStore
import tv.wfc.livestreamsales.application.storage.authorization.remote.IAuthorizationRemoteDataStore
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsDataStore
import tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.ILiveBroadcastingSettingsDataStore
import tv.wfc.livestreamsales.application.storage.paymentcardinformation.IPaymentCardInformationDataStore
import tv.wfc.livestreamsales.application.storage.products.IProductsDataStore
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderDataStore
import tv.wfc.livestreamsales.application.storage.userpersonalinformation.IUserInformationDataStore
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsDataStore

@Module
abstract class AppDataStoreModule {
    @Binds
    abstract fun bindIAuthorizationRemoteDataStore(
        remoteDataStore: AuthorizationRemoteDataStore
    ): IAuthorizationRemoteDataStore

    @Binds
    abstract fun bindIAuthorizationLocalDataStore(
        localDataStore: AuthorizationLocalDataStore
    ): IAuthorizationLocalDataStore

    @Binds
    @UserInformationRemoteDataStore
    internal abstract fun bindUserInformationRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.userpersonalinformation.remote.UserInformationRemoteDataStore
    ): IUserInformationDataStore

    @Binds
    @UserInformationLocalDataStore
    internal abstract fun bindUserInformationLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.userpersonalinformation.local.UserInformationLocalDataStore
    ): IUserInformationDataStore

    @Binds
    @PaymentCardInformationRemoteDataStore
    internal abstract fun bindPaymentCardInformationRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.paymentcardinformation.remote.PaymentCardInformationRemoteDataStore
    ): IPaymentCardInformationDataStore

    @Binds
    @BroadcastAnalyticsRemoteDataStore
    internal abstract fun bindBroadcastAnalyticsRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.features.livebroadcast.storage.remote.BroadcastAnalyticsRemoteDataStore
    ): IBroadcastAnalyticsDataStore

    @Binds
    @ApplicationSettingsLocalDataStore
    internal abstract fun bindApplicationSettingsLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.applicationsettings.local.ApplicationSettingsLocalDataStore
    ): IApplicationSettingsDataStore

    @Binds
    @BroadcastsRemoteDataStore
    internal abstract fun bindBroadcastsRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote.BroadcastsRemoteDataStore
    ): IBroadcastsDataStore

    @Binds
    @BroadcastsLocalDataStore
    internal abstract fun bindBroadcastsLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.broadcastsinformation.local.BroadcastsLocalDataStore
    ): IBroadcastsDataStore

    @Binds
    @ProductsRemoteDataStore
    internal abstract fun bindProductsRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.products.remote.ProductsRemoteDataStore
    ): IProductsDataStore

    @Binds
    @ProductsLocalDataStore
    internal abstract fun bindProductsLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.products.local.ProductsLocalDataStore
    ): IProductsDataStore

    @Binds
    @ProductsOrderRemoteDataStore
    internal abstract fun bindProductsOrderRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.productsorder.remote.ProductsOrderRemoteDataStore
    ): IProductsOrderDataStore

    @Binds
    @ProductsOrderLocalDataStore
    internal abstract fun bindProductsOrderLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.productsorder.local.ProductsOrderLocalDataStore
    ): IProductsOrderDataStore

    @Binds
    @LiveBroadcastingSettingsLocalDataStore
    internal abstract fun bindLiveBroadcastingSettingsLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.local.LiveBroadcastingSettingsLocalDataStore
    ): ILiveBroadcastingSettingsDataStore

    @Binds
    internal abstract fun bindIStreamChatMessageRestDataStore(
        dataStore: StreamChatMessageRestDataStore
    ): IStreamChatMessageRestDataStore

    @Binds
    internal abstract fun bindIStreamChatMessagePusherDataStore(
        dataStore: StreamChatMessagePusherDataStore
    ): IStreamChatMessagePusherDataStore
}