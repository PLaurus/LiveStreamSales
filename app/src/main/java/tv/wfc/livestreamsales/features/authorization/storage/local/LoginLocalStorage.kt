package tv.wfc.livestreamsales.features.authorization.storage.local

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.sharedpreferences.qualifiers.AuthorizationSharedPreferences
import tv.wfc.livestreamsales.features.authorization.storage.ILoginStorage
import javax.inject.Inject

class LoginLocalStorage @Inject constructor(
    @AuthorizationSharedPreferences
    private val authorizationSharedPreferences: SharedPreferences,
    @IoScheduler
    private val ioScheduler: Scheduler
): ILoginStorage {
    companion object{
        private const val LOGIN_SHARED_PREFERENCES_KEY = "login"
    }
    override fun getLogin(): Single<String> {
        return Single
            .fromCallable { getLoginFromSharedPreferences() ?: "" }
            .subscribeOn(ioScheduler)
    }

    override fun saveLogin(login: String): Completable {
        return Completable
            .fromCallable { saveLoginToSharedPreferences(login) }
            .subscribeOn(ioScheduler)
    }

    private fun saveLoginToSharedPreferences(login: String){
        authorizationSharedPreferences.edit{
            putString(LOGIN_SHARED_PREFERENCES_KEY, login)
        }
    }

    private fun getLoginFromSharedPreferences(): String? {
        return authorizationSharedPreferences.getString(LOGIN_SHARED_PREFERENCES_KEY, "")
    }
}