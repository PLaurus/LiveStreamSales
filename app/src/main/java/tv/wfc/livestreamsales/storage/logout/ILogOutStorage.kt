package tv.wfc.livestreamsales.storage.logout

import io.reactivex.rxjava3.core.Completable

interface ILogOutStorage {
    fun logOut(): Completable
}