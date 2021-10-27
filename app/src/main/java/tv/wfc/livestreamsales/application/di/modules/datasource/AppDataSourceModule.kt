package tv.wfc.livestreamsales.application.di.modules.datasource

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.datasource.mystream.IMyStreamDataSource
import tv.wfc.livestreamsales.application.datasource.mystream.MyStreamDataSource
import tv.wfc.livestreamsales.application.datasource.publicstream.IPublicStreamDataSource
import tv.wfc.livestreamsales.application.datasource.streamchatmessage.pusher.IStreamChatMessagePusherDataSource
import tv.wfc.livestreamsales.application.datasource.streamchatmessage.pusher.StreamChatMessagePusherDataSource
import tv.wfc.livestreamsales.application.datasource.streamchatmessage.rest.IStreamChatMessageRestDataSource
import tv.wfc.livestreamsales.application.datasource.streamchatmessage.rest.StreamChatMessageRestDataSource
import tv.wfc.livestreamsales.application.di.modules.datasource.qualifiers.*
import tv.wfc.livestreamsales.application.storage.applicationsettings.IApplicationSettingsDataStore
import tv.wfc.livestreamsales.application.storage.authorization.local.AuthorizationLocalDataStore
import tv.wfc.livestreamsales.application.storage.authorization.local.IAuthorizationLocalDataStore
import tv.wfc.livestreamsales.application.storage.authorization.remote.AuthorizationRemoteDataStore
import tv.wfc.livestreamsales.application.storage.authorization.remote.IAuthorizationRemoteDataStore
import tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.ILiveBroadcastingSettingsDataStore
import tv.wfc.livestreamsales.application.storage.paymentcardinformation.IPaymentCardInformationDataStore
import tv.wfc.livestreamsales.application.storage.products.IProductsDataStore
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderDataStore
import tv.wfc.livestreamsales.application.storage.userpersonalinformation.IUserInformationDataStore
import tv.wfc.livestreamsales.features.livebroadcast.storage.IBroadcastAnalyticsDataStore

@Module
interface AppDataSourceModule {
    @Binds
    fun bindIAuthorizationRemoteDataStore(
        remoteDataStore: AuthorizationRemoteDataStore
    ): IAuthorizationRemoteDataStore

    @Binds
    fun bindIAuthorizationLocalDataStore(
        localDataStore: AuthorizationLocalDataStore
    ): IAuthorizationLocalDataStore

    @Binds
    @UserInformationRemoteDataStore
    fun bindUserInformationRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.userpersonalinformation.remote.UserInformationRemoteDataStore
    ): IUserInformationDataStore

    @Binds
    @UserInformationLocalDataStore
    fun bindUserInformationLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.userpersonalinformation.local.UserInformationLocalDataStore
    ): IUserInformationDataStore

    @Binds
    @PaymentCardInformationRemoteDataStore
    fun bindPaymentCardInformationRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.paymentcardinformation.remote.PaymentCardInformationRemoteDataStore
    ): IPaymentCardInformationDataStore

    @Binds
    @BroadcastAnalyticsRemoteDataStore
    fun bindBroadcastAnalyticsRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.features.livebroadcast.storage.remote.BroadcastAnalyticsRemoteDataStore
    ): IBroadcastAnalyticsDataStore

    @Binds
    @ApplicationSettingsLocalDataStore
    fun bindApplicationSettingsLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.applicationsettings.local.ApplicationSettingsLocalDataStore
    ): IApplicationSettingsDataStore

    @Binds
    @PublicStreamRemoteDataSource
    fun bindPublicStreamRemoteDataSource(
        remoteDataSource: tv.wfc.livestreamsales.application.datasource.publicstream.PublicStreamRemoteDataSource
    ): IPublicStreamDataSource

    @Binds
    @PublicStreamLocalDataSource
    fun bindStreamLocalDataSource(
        localDataSource: tv.wfc.livestreamsales.application.datasource.publicstream.PublicStreamLocalDataSource
    ): IPublicStreamDataSource

    @Binds
    @ProductsRemoteDataStore
    fun bindProductsRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.products.remote.ProductsRemoteDataStore
    ): IProductsDataStore

    @Binds
    @ProductsLocalDataStore
    fun bindProductsLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.products.local.ProductsLocalDataStore
    ): IProductsDataStore

    @Binds
    @ProductsOrderRemoteDataStore
    fun bindProductsOrderRemoteDataStore(
        remoteDataStore: tv.wfc.livestreamsales.application.storage.productsorder.remote.ProductsOrderRemoteDataStore
    ): IProductsOrderDataStore

    @Binds
    @ProductsOrderLocalDataStore
    fun bindProductsOrderLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.productsorder.local.ProductsOrderLocalDataStore
    ): IProductsOrderDataStore

    @Binds
    @LiveBroadcastingSettingsLocalDataStore
    fun bindLiveBroadcastingSettingsLocalDataStore(
        localDataStore: tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.local.LiveBroadcastingSettingsLocalDataStore
    ): ILiveBroadcastingSettingsDataStore

    @Binds
    fun bindIStreamChatMessageRestDataSource(
        dataSource: StreamChatMessageRestDataSource
    ): IStreamChatMessageRestDataSource

    @Binds
    fun bindIStreamChatMessagePusherDataSource(
        dataSource: StreamChatMessagePusherDataSource
    ): IStreamChatMessagePusherDataSource

    @Binds
    fun bindIMyStreamDataSource(
        dataSource: MyStreamDataSource
    ): IMyStreamDataSource
}