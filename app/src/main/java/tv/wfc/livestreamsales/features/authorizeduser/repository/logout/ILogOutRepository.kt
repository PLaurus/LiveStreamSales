package tv.wfc.livestreamsales.features.authorizeduser.repository.logout

import io.reactivex.rxjava3.core.Completable

interface ILogOutRepository {
    fun logOut(): Completable
}