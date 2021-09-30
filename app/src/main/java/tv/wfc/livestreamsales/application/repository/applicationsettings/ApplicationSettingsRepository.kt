package tv.wfc.livestreamsales.application.repository.applicationsettings

import tv.wfc.livestreamsales.application.di.modules.datastore.qualifiers.ApplicationSettingsLocalDataStore
import tv.wfc.livestreamsales.application.storage.applicationsettings.IApplicationSettingsDataStore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ApplicationSettingsRepository @Inject constructor(
    @ApplicationSettingsLocalDataStore
    private val applicationSettingsLocalDataStore: IApplicationSettingsDataStore
): IApplicationSettingsRepository {
    override fun getIsGreetingShown(): Single<Boolean> {
        return applicationSettingsLocalDataStore.getIsGreetingShown()
    }

    override fun saveIsGreetingShown(isShown: Boolean): Completable {
        return applicationSettingsLocalDataStore.saveIsGreetingShown(isShown)
    }
}