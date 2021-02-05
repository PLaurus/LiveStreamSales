package tv.wfc.livestreamsales.application.storage.applicationsettings.local

import android.content.SharedPreferences
import androidx.core.content.edit
import tv.wfc.livestreamsales.application.di.modules.sharedpreferences.qualifiers.ApplicationSharedPreferences
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.storage.applicationsettings.IApplicationSettingsStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ApplicationSettingsLocalStorage @Inject constructor(
    @ApplicationSharedPreferences
    private val applicationSharedPreferences: SharedPreferences,
    @IoScheduler
    private val ioScheduler: Scheduler
): IApplicationSettingsStorage {
    companion object{
        private const val IS_GREETING_SHOWN_KEY = "is_greeting_shown"
    }

    /**
     * By default operates on IO Scheduler
     */
    override fun getIsGreetingShown(): Single<Boolean> {
        val isGreetingShown = getIsGreetingShownFromSharedPreferences()
        return Single
            .just(isGreetingShown)
            .subscribeOn(ioScheduler)
    }

    /**
     * By default operates on IO Scheduler
     */
    override fun saveIsGreetingShown(isShown: Boolean): Completable {
        return Completable
            .create{ emitter ->
                saveIsGreetingShownToSharedPreferences(isShown)
                emitter.onComplete()
            }
            .subscribeOn(ioScheduler)
    }

    private fun getIsGreetingShownFromSharedPreferences(): Boolean{
        return applicationSharedPreferences.getBoolean(IS_GREETING_SHOWN_KEY, false)
    }

    private fun saveIsGreetingShownToSharedPreferences(isShown: Boolean){
        applicationSharedPreferences.edit{
            putBoolean(IS_GREETING_SHOWN_KEY, isShown)
        }
    }
}