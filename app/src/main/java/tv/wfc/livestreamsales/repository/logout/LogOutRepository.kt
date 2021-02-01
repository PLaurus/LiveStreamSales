package tv.wfc.livestreamsales.repository.logout

import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.storage.qualifiers.LogOutRemoteStorage
import tv.wfc.livestreamsales.storage.logout.ILogOutStorage
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class LogOutRepository @Inject constructor(
    @LogOutRemoteStorage
    private val logOutRemoteStorage: ILogOutStorage
): ILogOutRepository {
    override fun logOut(): Completable = logOutRemoteStorage.logOut()
}