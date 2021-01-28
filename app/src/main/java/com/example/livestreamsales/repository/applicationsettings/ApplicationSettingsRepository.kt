package com.example.livestreamsales.repository.applicationsettings

import com.example.livestreamsales.di.components.app.modules.applicationsettings.qualifiers.ApplicationSettingsLocalStorage
import com.example.livestreamsales.storage.applicationsettings.IApplicationSettingsStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ApplicationSettingsRepository @Inject constructor(
    @ApplicationSettingsLocalStorage
    private val greetingLocalStorage: IApplicationSettingsStorage
): IApplicationSettingsRepository {
    override fun getIsGreetingShown(): Single<Boolean> {
        return greetingLocalStorage.getIsGreetingShown()
    }

    override fun saveIsGreetingShown(isShown: Boolean): Completable {
        return greetingLocalStorage.saveIsGreetingShown(isShown)
    }
}