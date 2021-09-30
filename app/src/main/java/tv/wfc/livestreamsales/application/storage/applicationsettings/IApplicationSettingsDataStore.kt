package tv.wfc.livestreamsales.application.storage.applicationsettings

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IApplicationSettingsDataStore {
    fun getIsGreetingShown(): Single<Boolean>
    fun saveIsGreetingShown(isShown: Boolean): Completable
}