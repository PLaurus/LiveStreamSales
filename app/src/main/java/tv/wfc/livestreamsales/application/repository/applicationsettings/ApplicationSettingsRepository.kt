package tv.wfc.livestreamsales.application.repository.applicationsettings

import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ApplicationSettingsLocalStorage
import tv.wfc.livestreamsales.application.storage.applicationsettings.IApplicationSettingsStorage
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