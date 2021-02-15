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
import tv.wfc.livestreamsales.application.repository.productsinformation.IProductsInformationRepository
import tv.wfc.livestreamsales.application.repository.productsinformation.ProductsInformationRepository

@Module
abstract class AppRepositoryModule {
    @ApplicationScope
    @Binds
    internal abstract fun provideIApplicationSettingsRepository(
        applicationSettingsRepository: ApplicationSettingsRepository
    ): IApplicationSettingsRepository

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationRepository(
        authorizationRepository: AuthorizationRepository
    ): IAuthorizationRepository

    @ApplicationScope
    @Binds
    internal abstract fun provideBroadcastsInformationRepository(
        broadcastsInformationRepository: BroadcastsInformationRepository
    ): IBroadcastsInformationRepository

    @ApplicationScope
    @Binds
    internal abstract fun provideProductsInformationRepository(
        productsInformationRepository: ProductsInformationRepository
    ): IProductsInformationRepository
}