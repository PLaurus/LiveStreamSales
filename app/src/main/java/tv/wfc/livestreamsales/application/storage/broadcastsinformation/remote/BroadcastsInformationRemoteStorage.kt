package tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsInformationApi
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage
import javax.inject.Inject

class BroadcastsInformationRemoteStorage @Inject constructor(
    private val broadcastsInformationApi: IBroadcastsInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastsInformationStorage {
    override fun getBroadcasts(): Single<List<BroadcastInformation>> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single.just(listOf(
                BroadcastInformation(
                    0,
                    "Отдай мне свои деньги 1",
                    "Цыган вымогает деньги 1",
                    DateTime.now().minusDays(2),
                    ""
                ),
                BroadcastInformation(
                    0,
                    "Отдай мне свои деньги 2",
                    "Цыган вымогает деньги 2",
                    DateTime.now().minusDays(1),
                    "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg"
                ),
                BroadcastInformation(
                    0,
                    "Отдай мне свои деньги 3",
                    "Цыган вымогает деньги 3",
                    DateTime.now().plusDays(1),
                    "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg"
                ),
                BroadcastInformation(
                    0,
                    "Отдай мне свои деньги 4",
                    "Цыган вымогает деньги 4",
                    DateTime.now().plusDays(2),
                )
            ))
        }

        return broadcastsInformationApi
            .getBroadcasts()
            .flatMap{ response ->
                val broadcasts = response.body()?.data

                if(broadcasts != null){
                    Single.just(broadcasts)
                } else{
                    throw Exception("Failed to receive broadcasts from server!")
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun saveBroadcasts(broadcasts: List<BroadcastInformation>): Completable {
        return Completable.create {
            it.onError(NotImplementedError())
        }
    }

    override fun getBroadcast(id: Long): Single<BroadcastInformation> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single.just(BroadcastInformation(
                0,
                "Отдай мне свои деньги 2",
                "Цыган вымогает деньги 2",
                DateTime.now().minusDays(1),
                "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg",
                "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvEEA1hs_y3yb6d5oamTJ1-UJ7wHlzxPiSFH5vnVqLT2-L4WDKoWDHnxu5NF2jD_xwjOhIOBW-rRpr4u7n-A12jUi8TYE5bIDMYxhcWBZnhNNKJkBT-MjIqFdGpjCHyZnxFCZFmLs3bd1hQFui-_S50iL4qItn5jfrwFw9oP9qFatip-vcZANDLnRLcx1fRGUcd7OWScJvYeIvpTNCbza49yhGBvBCuiPNle73znmmrg4ZXQt6wBYEVjBHhfN-EfTjATbhvJ2uBel65UfJvjdXmARdR3w6iJy7i_r_Nauonq2bn8221ERXiWKH6DS71QtmlYGVbzwMTfGPAqUOnIGQ6wLguRvRvWHnfdIEzrcSzekel6j-J24kjO39HSQKyiZ8F-4IOsu7zK2VHfLnC57oXnFxKto0BRFUVE5NEULNk9BVma-6QApvnfHt7vR4FuGdQbO1l0GDqOZCAuILZEKhmVUk6V5C6WH0tC4skOrr5QWeeMoOSNoFu7ggy1EwPwAXLhzDfZsMNh5tJTjmCSADSbmaRw86Ee2M5DIyOL0eAQK7iZeYjBAY1OBFRPpAH1zAG8YFF1qSxdvolua8QaHDYt7cerkamoZFAx_8ZoPxV2smH1n_eAzYvTvKjwpLcBOMUGg9VlqnjB-SHky1Qnw9qASc2BDdiEoBgOq3yutIYNwnk-GAf0aearjR4yO0Z9ewauuyyfqgptVdrXc6ANNa-T8dmA8AawHrZ2Tkra00zZzEunCCbcvPOl6y_9psTtq2gw4Q6Qg-iCQIDUmRl5nohFE7QiRcgzvh8Va-YqGoJxozYPB8jHAGZSPgqVRg0rfAEiVBIQ8JvZqsgVMIbmQ1NSdKcRwxoMSLlauGoMNrmxe-Oe.m3u8",
                8
            ))
        }

        return broadcastsInformationApi
            .getBroadcast(id)
            .flatMap { response ->
                val broadcast = response.body()?.data

                if(broadcast != null){
                    Single.just(broadcast)
                } else{
                    throw Exception("Failed to receive broadcast from server!")
                }
            }
            .subscribeOn(ioScheduler)
    }
}