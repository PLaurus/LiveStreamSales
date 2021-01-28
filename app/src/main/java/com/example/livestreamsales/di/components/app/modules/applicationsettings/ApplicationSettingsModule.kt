package com.example.livestreamsales.di.components.app.modules.applicationsettings

import com.example.livestreamsales.di.components.app.modules.applicationsettings.qualifiers.ApplicationSettingsLocalStorage
import com.example.livestreamsales.di.scopes.ApplicationScope
import com.example.livestreamsales.repository.applicationsettings.ApplicationSettingsRepository
import com.example.livestreamsales.repository.applicationsettings.IApplicationSettingsRepository
import com.example.livestreamsales.storage.applicationsettings.IApplicationSettingsStorage
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationSettingsModule {
    @ApplicationScope
    @Binds
    @ApplicationSettingsLocalStorage
    internal abstract fun provideApplicationSettingsLocalStorage(
        applicationSettingsLocalStorage: com.example.livestreamsales.storage.applicationsettings.local.ApplicationSettingsLocalStorage
    ): IApplicationSettingsStorage

    @ApplicationScope
    @Binds
    internal abstract fun provideIApplicationSettingsRepository(
        applicationSettingsRepository: ApplicationSettingsRepository
    ): IApplicationSettingsRepository
}