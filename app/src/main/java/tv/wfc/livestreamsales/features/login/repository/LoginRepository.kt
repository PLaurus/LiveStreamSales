package tv.wfc.livestreamsales.features.login.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.features.login.di.modules.storage.qualifiers.LoginLocalStorage
import tv.wfc.livestreamsales.features.login.storage.ILoginStorage
import javax.inject.Inject

class LoginRepository @Inject constructor(
    @LoginLocalStorage
    private val loginLocalStorage: ILoginStorage
): ILoginRepository {
    override fun getLogin(): Single<String> {
        return loginLocalStorage.getLogin()
    }

    override fun saveLogin(login: String): Completable {
        return loginLocalStorage.saveLogin(login)
    }
}