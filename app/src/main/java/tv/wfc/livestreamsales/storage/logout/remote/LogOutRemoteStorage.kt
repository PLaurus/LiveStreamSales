package tv.wfc.livestreamsales.storage.logout.remote

import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.network.rest.api.authorized.ILogOutApi
import tv.wfc.livestreamsales.storage.logout.ILogOutStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class LogOutRemoteStorage @Inject constructor(
    private val logOutApi: ILogOutApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): ILogOutStorage {
    override fun logOut(): Completable {
        return logOutApi
            .logOut()
            .ignoreElement()
            .subscribeOn(ioScheduler)
    }
}