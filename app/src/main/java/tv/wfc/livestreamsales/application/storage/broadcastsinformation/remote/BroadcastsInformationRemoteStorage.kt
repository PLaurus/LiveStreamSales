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
                "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvIEMnzt4WIp7lbv0RD1r45J87qyeOQEW_I-xezUuFxFtsODWAEIhyiwmoMWN6GnzjVUJvcaJXkEAhuB-_Aa9vdVb4xk4FV123FaXQuYTMs1iQfXMTD5x3fqgpNIV2xJeMNdPhz6VMqyh0awNfEnkNvzLcCGpH0j2woMUhZVAuXmimVP5l6iACnh_AKYhk_5vC6A5WZVigacaTVeQ1nMnldZYBe-eWE9gBKhZP12DX6fyCuwCljZpdWQzSGAChwDHDBlJUpF5CFMjQW0_4F9pwR6JiN3SQv4AvfnQuyL1sQjCEISG5dW7tlXzB0okDwvKoMTW4vxt-1QLfDnGcD7V5g0RgXkmkk2TX5ZTDTmg3KtFjxMbXJayBlRxyG6ugdAyVHMh82ARowrITBUwHBLcglPMRcudw7UdH4Ca79HC6eDWd4bshcusJweXqtZDPendMxWHyJHWPlaGqis2MBD9VH-lEtz5w3JXmfYVOr0d9zPQQnwPR0s7qFU8G63w_l7ckl5fPDSlcto4YAtkPhiE5nMuw99FOhaDfsPevUhCGu5WkXbihtEPjwN_aW8ctUM9aiQA5vV_PFHMvXTaczIsemBWW53DresgyQnSYveLQvuSzyTkqXnHF_IQ125mkN_X8x3yFZwLmgqtg7HOR6oDpAh7oHoBtqXdAR6h1drZNiomBkFe8CxLbqS5JWgyoxh23ix4F1EDvn2k7MSPgw1ToPid7VMfMbPc5hQvkTSNLEoiyLtAyEsqceX_XsXu3OBOH5tx3_UKaGTNz06Wl7OBEuNyupKZ0_53FypYtlUAqJTcMs3Q9Ot2uUgNXjSays0sSqdcFQSEDsGuAWQ9bYFY8GrONiBI-waDJXpw_Be1HSWRvB2fA.m3u8",
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