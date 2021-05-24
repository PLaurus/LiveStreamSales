package tv.wfc.livestreamsales.features.authorization.storage

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ILoginStorage {
    fun getLogin(): Single<String>
    fun saveLogin(login: String): Completable
}