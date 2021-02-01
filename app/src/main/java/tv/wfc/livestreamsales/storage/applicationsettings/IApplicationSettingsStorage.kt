package tv.wfc.livestreamsales.storage.applicationsettings

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IApplicationSettingsStorage {
    fun getIsGreetingShown(): Single<Boolean>
    fun saveIsGreetingShown(isShown: Boolean): Completable
}