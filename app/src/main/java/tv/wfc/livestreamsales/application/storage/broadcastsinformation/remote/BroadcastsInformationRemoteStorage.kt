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
                "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvIEnuzotXkLJY2uxULHPp1mJ9IGRmqesrLmVmyGI4N5KNJD2P4AXstXmyllVvweuv-Lz0eVPRYjyfI7lwkLGGfTIpJx5C42fTxWMwCS_jF0Iu0Z5JflJbD4avnPCH9NF3FCNOuSBf9h27ISFWBeU4r4F0UzBCI8Hrr_KeTknAIaMG03-FeyciF-iQq4uU1Vk07UrUQp87G18UJZ8x2BusgN9WmotkP_yQ_2oycxmKQ-do5tfyhFo_UaeWD7STr5DEKh_DQAX_SmOLVVGuvuIogUtfNAYxffxCk3_ieej8NpIa8jXQuPGG6dvAWpqAQTR3N-kL0rMnry7yDlhKySYDLd8j9XlxA8bTk5c_oHIbbYTW9G5j3h7OO3PZ9EzF9UXe0k9yEvwVG0gD5CK4PABNhA9vwRIDErqATLgZeQ6QzmeJdQsa7Tbp0HRLi66MS5FZcHluS-l4OOJeXPTElP1Z-l21hg31UDSr_BMuQbgZfPr14cy1f58wU3O83XmU0vIEGw20A-wv8C6HzYFjoRf6L9djrfoC3ymfv6DZLKHHsSBjR0xVjBgdFKZaLLak_Brk4xBZS9ST_UY3rttRio5nCpf29ChOICOb7VtrlzhdCUGBIZ61xPds0yVzPUnD6SY-PRlTp7NjT-P1Cx1vbnS39HpJFp2vmTkcdZZOQi5spGQHFqbneukAMFyEGDmqOsTa1jzgS2lSgMl8BNOYzCs9Oo57Yrtzbx3w3i1TyM6-1H8zG0NdsTlluB9AR3cWCqMNO6DvqDaYjoMKKIvKPBia_cDn98vQOnK5de1V2aki26ebUFB24If88S_TQQ0y4RjSIY9hQSELB7U6C08f6fmzDENQ-j2YAaDD_SPR3lzbX1WuQxkg.m3u8",
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