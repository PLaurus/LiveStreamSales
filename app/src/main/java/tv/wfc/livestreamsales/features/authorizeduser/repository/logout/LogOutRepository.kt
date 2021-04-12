package tv.wfc.livestreamsales.features.authorizeduser.repository.logout

import tv.wfc.livestreamsales.application.di.modules.optionals.storage.qualifiers.LogOutRemoteStorage
import tv.wfc.livestreamsales.features.authorizeduser.storage.logout.ILogOutStorage
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class LogOutRepository @Inject constructor(
    @LogOutRemoteStorage
    private val logOutRemoteStorage: ILogOutStorage
): ILogOutRepository {
    override fun logOut(): Completable = logOutRemoteStorage.logOut()
}