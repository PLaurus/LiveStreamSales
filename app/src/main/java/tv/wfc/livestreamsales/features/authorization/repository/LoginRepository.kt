package tv.wfc.livestreamsales.features.authorization.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.features.authorization.di.modules.storage.qualifiers.LoginLocalStorage
import tv.wfc.livestreamsales.features.authorization.storage.ILoginStorage
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