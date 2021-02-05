package tv.wfc.livestreamsales.features.login.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ILoginRepository {
    fun getLogin(): Single<String>
    fun saveLogin(login: String): Completable
}