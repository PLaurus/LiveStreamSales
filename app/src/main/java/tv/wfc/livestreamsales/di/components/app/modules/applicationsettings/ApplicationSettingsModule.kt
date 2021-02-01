package tv.wfc.livestreamsales.di.components.app.modules.applicationsettings

import tv.wfc.livestreamsales.di.components.app.modules.applicationsettings.qualifiers.ApplicationSettingsLocalStorage
import tv.wfc.livestreamsales.di.scopes.ApplicationScope
import tv.wfc.livestreamsales.repository.applicationsettings.ApplicationSettingsRepository
import tv.wfc.livestreamsales.repository.applicationsettings.IApplicationSettingsRepository
import tv.wfc.livestreamsales.storage.applicationsettings.IApplicationSettingsStorage
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationSettingsModule {
    @ApplicationScope
    @Binds
    @ApplicationSettingsLocalStorage
    internal abstract fun provideApplicationSettingsLocalStorage(
        applicationSettingsLocalStorage: tv.wfc.livestreamsales.storage.applicationsettings.local.ApplicationSettingsLocalStorage
    ): IApplicationSettingsStorage

    @ApplicationScope
    @Binds
    internal abstract fun provideIApplicationSettingsRepository(
        applicationSettingsRepository: ApplicationSettingsRepository
    ): IApplicationSettingsRepository
}