package tv.wfc.livestreamsales.application.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.application.repository.applicationsettings.ApplicationSettingsRepository
import tv.wfc.livestreamsales.application.repository.applicationsettings.IApplicationSettingsRepository
import tv.wfc.livestreamsales.application.repository.authorization.AuthorizationRepository
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.application.repository.livebroadcastingsettings.ILiveBroadcastingSettingsRepository
import tv.wfc.livestreamsales.application.repository.livebroadcastingsettings.LiveBroadcastingSettingsRepository
import tv.wfc.livestreamsales.application.repository.mystream.IMyStreamRepository
import tv.wfc.livestreamsales.application.repository.mystream.MyStreamRepository
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.IPaymentCardInformationRepository
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.PaymentCardInformationRepository
import tv.wfc.livestreamsales.application.repository.products.IProductsRepository
import tv.wfc.livestreamsales.application.repository.products.ProductsRepository
import tv.wfc.livestreamsales.application.repository.productsorder.IProductsOrderRepository
import tv.wfc.livestreamsales.application.repository.productsorder.ProductsOrderRepository
import tv.wfc.livestreamsales.application.repository.publicstream.IPublicStreamRepository
import tv.wfc.livestreamsales.application.repository.publicstream.PublicStreamRepository
import tv.wfc.livestreamsales.application.repository.streamchatmessage.IStreamChatMessageRepository
import tv.wfc.livestreamsales.application.repository.streamchatmessage.StreamChatMessageRepository
import tv.wfc.livestreamsales.application.repository.userinformation.IUserInformationRepository
import tv.wfc.livestreamsales.application.repository.userinformation.UserInformationRepository
import tv.wfc.livestreamsales.features.livebroadcast.repository.BroadcastAnalyticsRepository
import tv.wfc.livestreamsales.features.livebroadcast.repository.IBroadcastAnalyticsRepository

@Module
interface AppRepositoryModule {
    @ApplicationScope
    @Binds
    fun bindIUserInformationRepository(
        repository: UserInformationRepository
    ): IUserInformationRepository

    @ApplicationScope
    @Binds
    fun bindIPaymentCardInformationRepository(
        repository: PaymentCardInformationRepository
    ): IPaymentCardInformationRepository

    @ApplicationScope
    @Binds
    fun bindIBroadcastAnalyticsRepository(
        repository: BroadcastAnalyticsRepository
    ): IBroadcastAnalyticsRepository

    @ApplicationScope
    @Binds
    fun bindIApplicationSettingsRepository(
        repository: ApplicationSettingsRepository
    ): IApplicationSettingsRepository

    @ApplicationScope
    @Binds
    fun bindIAuthorizationRepository(
        repository: AuthorizationRepository
    ): IAuthorizationRepository

    @ApplicationScope
    @Binds
    fun bindIBroadcastsRepository(
        repository: PublicStreamRepository
    ): IPublicStreamRepository

    @ApplicationScope
    @Binds
    fun bindIProductsRepository(
        repository: ProductsRepository
    ): IProductsRepository

    @ApplicationScope
    @Binds
    fun bindIProductsOrderRepository(
        repository: ProductsOrderRepository
    ): IProductsOrderRepository

    @ApplicationScope
    @Binds
    fun bindIStreamChatMessageRepository(
        repository: StreamChatMessageRepository
    ): IStreamChatMessageRepository

    @ApplicationScope
    @Binds
    fun bindILiveBroadcastingSettingsRepository(
        repository: LiveBroadcastingSettingsRepository
    ): ILiveBroadcastingSettingsRepository

    @ApplicationScope
    @Binds
    fun bindIMyStreamRepository(
        repository: MyStreamRepository
    ): IMyStreamRepository
}