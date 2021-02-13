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
                "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvsEwrE_VOjlwx3C9lmZz4G_GNrVEytm4bEVRY21V0GNpccSBdIGUkCHItlwg_5BybhNCq3dVrN-xaJnTCYGJqYbrtSWag6f7K2uFQGlWUSgP2W23mE45ssepdFUpp_3wXOD2aZ6xkR3aH1ThVRAfXHjJvLnREvhR3jfko8R5_j3UpBpxIo4-fbNXd_fTZO9uE562bPl3BpK3OKN_c7JqyGwxejbbWb0iUWeOP95DwxYBraVBsZ6gwTpdclxvZeXhUusoE8QaiU7PjI-FhhLY36PC40hvwqqCRXiXeUYSG6GQmZclJe-cApmP1_RygJQ2S9kYP6GF0WMdwB0G7Bytp4TY3UASFFx7p1Lz3a5ZyzLBf-snSuXEMwU-q8Iy2Ykf5rmflm6pxxu4e1TaRpt5dyktpQNhp0p9_rUSovI-uO-gnUWJOIzIkEUgcJvK_V6fHFp_DOj4NZrO3xHeiABKcGXoIbg_qpJU9MFVwFH-Dj7MQnfdQx_0eDjW9wtoazrB_TES4SDPByV8Gp8mRvxFVGmOK2deib00n5zQtn9deiCfwodVLdHaWVfccJRvuPr5m2UIkR7vo2QyE-74xwQVxj3PrXUBVtvoyIHU0L1ltFb5p3L07MKjpq_OxdMjOMyTa5KOD3workcc0lVtEeqP1C-gs5glhocblyIH6ezhCeIMOEiwuuZ0vPzklfTsR9om4u5GmdCYXs5Cd-3i-Slx75tA9yQkiHhsyriLbQawm1rmG47lLx5FIpcG9QbaTJZEuGLFbc4-kEp2eRe6sCf_SkdbHk4SKCnAQ2-yJsccNoJCeiYbPUl-ZrmqA1VZrr4lSVwoILT50vKsYWzF30SEKqhsW31qlFGd4Cb27Ex0uYaDJ8mmMoylZ6nyl_PNg.m3u8",
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