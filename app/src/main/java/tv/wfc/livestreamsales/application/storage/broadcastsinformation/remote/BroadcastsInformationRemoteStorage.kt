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
                "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvIENSCQM0-g3BpDRyfewrfcPcUWs-p77Wpteg5wvBPVngc_oalJ3zDJLQR5NB8486UrKlXEmBkODhtYqd9SkqusIT5J2xY2B__EijLbG6L30UrUdhskS2A4jyGJ3EVKr35mEuSfwCkeYASk5HfgaleRdZPkM52jFUzue6bZLGFQynClmpXIhez4Jb6E_T0wJUomZmmmI2WgfG-PP4v1uvbidslORB-Yje7p2U8YX2Zey5gVyIZ8pEzNwfk_6-aC88UWafzL653ZHjluzjcxcI8brFlo4f-FrdRmpBIDsrVvDwuttYcct1sMaIc42WaDvoOQr7rwmJH3-f8LbgoewsabKQkRgYnKMOrO9uYA-CH8KgRNOKKX-5NHCdkwhaUp7pTQtRP95NVasYGITYs2-vonZJjzCrDZNeh6apo8XVOdUFUWATVx8CwYHXkTyojJNeBtRFO71HouJebaZi0QDgIf2SXdulLHlGM4cEIvnFVb09fvUDNF5-aDZ7RRD8xYrbKrQA2zXIn6eyzs5dcE7KOpFMjGLucX9s-S6mRzkqanB4E5LqIWz1EHlvag1cc0NAttGX0wNcQn96K0-MEDeKZrbadUhTkh0t0_doWbGrNnARPCG6CBbyc5VnU81ZDPD-EgF0kGbN8V--HmpB6gU6tHF7eNBa4kkx5yX8CTgA3AQT-dRgFX12h8pWr9yjIUl1e1-msnX2C_O3DePAe5pDHHTjqtOMXFN5U123UNuYkFxGYxBG4pqqH_10jWAFYuvzgMZzTOFJM60FtBg3c1XZubAoQ5L0wsGWq0ExjkKy3ztamBE7LDZzS8TOEwLmt4yX1UnzoSEAyiWiehdmW9wRDctjvoxQsaDPUiJO-IScLxTpvUtA.m3u8",
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