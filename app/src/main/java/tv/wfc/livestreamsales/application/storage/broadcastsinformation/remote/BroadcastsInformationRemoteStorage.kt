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
                "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvEEVmTxJl8K6tH4q9lprLEYuLdjITeg2qaEihKgD36z_A-aganuybQz8UTJs_SxSWvTOWAIFlqngIfKaVY0HXzKQlLj_kVtrf1cvma-UNey5cz27D6UUnIcXUNyx2qA9P4N4c6vXkscnH6Jgp83fxxZQeHmLNMQPw2qAMKyBRtZIBDCbt1BZIvKgeLuTsjNBMpWJs0aWOukcf1VAcAhMmRAIM0HcYU3Tj1f2WHcZT5_HwegeEJeOvotcW_7c6pUb88xNwhzW8Zq3Yi0v--b7K8zFl66b0xnv0DD56NLmfjJmvjl0aDNbtEf5BxOXZ5N2Ni3jwATl2kLthx4vuSimg8NGY6b66pGoeIhC5fmdgXoG_balShjRx_dmO0t4zz6ePMZgi0q5bHtTBh2wlWoPQYoO0phvzcigf_ngwkyHxUm2m6_aWiNsT-nGKrSoj-1bH980CFOqAWqgrxfAM10H0dn_slEhlSf1y7wJyxxiaNlIkKANFI0f10IWBm6NjFx8nX5yrGnvZSCVOrRKEA6qE6HgvUm3BqeJWFQpl_6ev86B3QvpSHb1Ypx7OjflqP75bXGkfXFDYYXjoRJKN2N5cA4jXYzZYed2HGH65SGv4MVZ8PpYgRGOjvDdhhJSFZKZIcfoqhd0jwugaBr409RtAvFJN2wwyAyVsNo3ycUAFIjf-UlwLlDRuKGU6gSTTf7R4SyUzl1G1VetC77oPUEL0WlYDbXlsg18ABXmDYnGKH04r_0G-8XkkHiwvFoH7O7qkrKZZfWl-6PCwpJ7wmJfeQwMSw_kbPT-tpfIo7De4_VqBYbXc_Lz8eRU7gpCSZtCs70ABIQiLBG0CMI_FyAktR6-pnG_RoMFpc0iAIg9nlmggN2.m3u8",
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