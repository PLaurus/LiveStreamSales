package tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsInformationApi
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage
import javax.inject.Inject

class BroadcastsInformationRemoteStorage @Inject constructor(
    private val broadcastsInformationApi: IBroadcastsInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastsInformationStorage {
    override fun getBroadcasts(): Single<List<BroadcastBaseInformation>> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single.just(listOf(
                BroadcastBaseInformation(
                    0,
                    "Отдай мне свои деньги 1",
                    "Цыган вымогает деньги 1",
                    DateTime.now().minusDays(2),
                    ""
                ),
                BroadcastBaseInformation(
                    0,
                    "Отдай мне свои деньги 2",
                    "Цыган вымогает деньги 2",
                    DateTime.now().minusDays(1),
                    "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg"
                ),
                BroadcastBaseInformation(
                    0,
                    "Отдай мне свои деньги 3",
                    "Цыган вымогает деньги 3",
                    DateTime.now().plusDays(1),
                    "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg"
                ),
                BroadcastBaseInformation(
                    1,
                    "Отдай мне свои деньги 4",
                    "Цыган вымогает деньги 4",
                    DateTime.now().plusDays(2),
                )
            ))
        }

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