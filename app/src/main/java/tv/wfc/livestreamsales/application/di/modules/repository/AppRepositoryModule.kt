package tv.wfc.livestreamsales.application.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.application.repository.applicationsettings.ApplicationSettingsRepository
import tv.wfc.livestreamsales.application.repository.applicationsettings.IApplicationSettingsRepository
import tv.wfc.livestreamsales.application.repository.authorization.AuthorizationRepository
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.BroadcastsInformationRepository
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.IBroadcastsInformationRepository
import tv.wfc.livestreamsales.application.repository.chat.ChatRepository
import tv.wfc.livestreamsales.application.repository.chat.IChatRepository
import tv.wfc.livestreamsales.application.repository.livebroadcastingsettings.ILiveBroadcastingSettingsRepository
import tv.wfc.livestreamsales.application.repository.livebroadcastingsettings.LiveBroadcastingSettingsRepository
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.IPaymentCardInformationRepository
import tv.wfc.livestreamsales.application.repository.paymentcardinformation.PaymentCardInformationRepository
import tv.wfc.livestreamsales.application.repository.products.IProductsRepository
import tv.wfc.livestreamsales.application.repository.products.ProductsRepository
import tv.wfc.livestreamsales.application.repository.productsorder.IProductsOrderRepository
import tv.wfc.livestreamsales.application.repository.productsorder.ProductsOrderRepository
import tv.wfc.livestreamsales.application.repository.userpersonalinformation.IUserPersonalInformationRepository
import tv.wfc.livestreamsales.application.repository.userpersonalinformation.UserPersonalInformationRepository
import tv.wfc.livestreamsales.features.livebroadcast.repository.BroadcastAnalyticsRepository
import tv.wfc.livestreamsales.features.livebroadcast.repository.IBroadcastAnalyticsRepository

@Module
abstract class AppRepositoryModule {
    @ApplicationScope
    @Binds
    internal abstract fun bindUserPersonalInformationRepository(
        repository: UserPersonalInformationRepository
    ): IUserPersonalInformationRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindPaymentCardInformationRepository(
        repository: PaymentCardInformationRepository
    ): IPaymentCardInformationRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindBroadcastAnalyticsRepository(
        repository: BroadcastAnalyticsRepository
    ): IBroadcastAnalyticsRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindApplicationSettingsRepository(
        repository: ApplicationSettingsRepository
    ): IApplicationSettingsRepository

    @ApplicationScope
    @Binds
    abstract fun bindAuthorizationRepository(
        repository: AuthorizationRepository
    ): IAuthorizationRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindBroadcastsInformationRepository(
        repository: BroadcastsInformationRepository
    ): IBroadcastsInformationRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindProductsRepository(
        repository: ProductsRepository
    ): IProductsRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindProductsOrderRepository(
        repository: ProductsOrderRepository
    ): IProductsOrderRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindChatRepository(
        repository: ChatRepository
    ): IChatRepository

    @ApplicationScope
    @Binds
    internal abstract fun bindLiveBroadcastingSettingsRepository(
        repository: LiveBroadcastingSettingsRepository
    ): ILiveBroadcastingSettingsRepository
}