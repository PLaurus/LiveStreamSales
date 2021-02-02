package tv.wfc.livestreamsales.storage.broadcastsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.model.application.broadcastinformation.BroadcastBaseInformation
import tv.wfc.livestreamsales.network.rest.api.notauthorized.IBroadcastsInformationApi
import tv.wfc.livestreamsales.storage.broadcastsinformation.IBroadcastsInformationStorage
import javax.inject.Inject

class BroadcastsInformationRemoteStorage @Inject constructor(
    private val broadcastsInformationApi: IBroadcastsInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastsInformationStorage {
    override fun getBroadcasts(): Single<List<BroadcastBaseInformation>> {
        return broadcastsInformationApi
            .getBroadcasts()
            .flatMap{ response ->
                val broadcasts = response.body()

                if(broadcasts != null){
                    Single.just(broadcasts)
                } else{
                    throw Exception("Failed to receive data from server!")
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun saveBroadcasts(broadcasts: List<BroadcastBaseInformation>): Completable {
        return Completable.create {
            it.onError(NotImplementedError())
        }
    }
}