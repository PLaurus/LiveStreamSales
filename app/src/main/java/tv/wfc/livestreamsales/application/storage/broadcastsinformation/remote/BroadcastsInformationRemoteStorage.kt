package tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsInformationStorage
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsInformationApi
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
                "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvwE0YpHNJk4x5AQmBI4Lr0sUhJA8ottBmXAvDtifR0S0YB1q8C1ODTeg9cJ_BgSv6kNAe7lvf9GUFnabu61RNpFCJOs1CR3ABIaQgHphKR4n71dV3jALbSc3OIBK0aEKdd7lgnyINRhyezvowgb1nL8qUw2Jtz5PRszXHWZ8jZinJU7-BSjnMZjwJwL2t7PpqhlxYE9f-1IjveCY5Q_hBHTxFujBimTmOm8JOUaUbRo5WZRgfqAZw-0eAfwmkivrSE2q6kmEN47vRvXd15jm-_93DYvj4O2EHcbbjTIU-iHWfBM6E6iO4CMb7E2upvNGVuZ3m4lAvotr-bdmfPNYcQ2DIDw37E3U2psVF2z9uuPdwNB850UbL80_ojZfpBMenLymnIUKjKqxauPbif5cflwBhjwiJpef9-NQT8kMygJyH5A7h8UFgkelpAN6bUBE4LidjZalzfXUBu7jhkuMhulnhCjmD_8ehDBCaXEWmH278hvpuS_LSm6XahY1itoN72kiOWGAFrGQsADaGVA_-mt5mErGqV0Pnsr4yjbeseIP7gFiHejwsKyo83awREnmZ-T-Nf1a26-dU9y934Ee_WOct2J0kJc6KLY8I-V6RSS-4LjRrMuoOzjJb9e2dDjHTCUJECZIkovb3_1VjXAOXuHANQOwMO0NFID9cV4-fPuJI2EyJqjRVWjvkEPojBe2cux2IbytVWY3eBB8fQB2C1kisHgorttKqD_BgXUJYHsj17iiQEAM8RYUvJE0XizazVZ64boHtUFckd8zAB5SUynynI5VnyRmLqt7l-U7pHmYN8P6SJrzvtPtlVTXYMlLfx6M0MTZG4DLi7sVZi5EhDm84ME_JqDXSI6t_WVvgS_Ggy8nboUsL2IZZr6OCA.m3u8",
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