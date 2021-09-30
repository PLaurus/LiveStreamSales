package tv.wfc.livestreamsales.application.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.application.repository.applicationsettings.ApplicationSettingsRepository
import tv.wfc.livestreamsales.application.repository.applicationsettings.IApplicationSettingsRepository
import tv.wfc.livestreamsales.application.repository.authorization.AuthorizationRepository
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.BroadcastsRepository
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.IBroadcastsRepository
import tv.wfc.livestreamsales.application.repository.livebroadcastingsettings.ILiveBroadcastingSettingsRepository
import tv.wfc.livestreamsales.application.repository.livebroadcastingsettings.LiveBroadcastingSettingsRepository
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.IPaymentCardInformationRepository
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.PaymentCardInformationRepository
import tv.wfc.livestreamsales.application.repository.products.IProductsRepository
import tv.wfc.livestreamsales.application.repository.products.ProductsRepository
import tv.wfc.livestreamsales.application.repository.productsorder.IProductsOrderRepository
import tv.wfc.livestreamsales.application.repository.productsorder.ProductsOrderRepository
import tv.wfc.livestreamsales.application.repository.streamchatmessage.IStreamChatMessageRepository
import tv.wfc.livestreamsales.application.repository.streamchatmessage.StreamChatMessageRepository
import tv.wfc.livestreamsales.application.repository.userinformation.IUserInformationRepository
import tv.wfc.livestreamsales.application.repository.userinformation.UserInformationRepository
import tv.wfc.livestreamsales.features.livebroadcast.repository.BroadcastAnalyticsRepository
import tv.wfc.livestreamsales.features.livebroadcast.repository.IBroadcastAnalyticsRepository

@Module
abstract class AppRepositoryModule {
    @ApplicationScope
    @Binds
    internal abstract fun bindIUserInformationRepository(
        repository: UserInformationRepository
    ): IUserInformationRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindIPaymentCardInformationRepository(
        repository: PaymentCardInformationRepository
    ): IPaymentCardInformationRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindIBroadcastAnalyticsRepository(
        repository: BroadcastAnalyticsRepository
    ): IBroadcastAnalyticsRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindIApplicationSettingsRepository(
        repository: ApplicationSettingsRepository
    ): IApplicationSettingsRepository

    @ApplicationScope
    @Binds
    abstract fun bindIAuthorizationRepository(
        repository: AuthorizationRepository
    ): IAuthorizationRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindIBroadcastsRepository(
        repository: BroadcastsRepository
    ): IBroadcastsRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindIProductsRepository(
        repository: ProductsRepository
    ): IProductsRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindIProductsOrderRepository(
        repository: ProductsOrderRepository
    ): IProductsOrderRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindIStreamChatMessageRepository(
        repository: StreamChatMessageRepository
    ): IStreamChatMessageRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindILiveBroadcastingSettingsRepository(
        repository: LiveBroadcastingSettingsRepository
    ): ILiveBroadcastingSettingsRepository
}