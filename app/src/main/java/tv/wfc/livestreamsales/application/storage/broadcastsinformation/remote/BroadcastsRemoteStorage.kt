package tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast
import tv.wfc.livestreamsales.application.model.exception.storage.NoSuchDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsStorage
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsApi
import tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts.Stream
import javax.inject.Inject

class BroadcastsRemoteStorage @Inject constructor(
    private val broadcastsApi: IBroadcastsApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IBroadcastsStorage {
    override fun getBroadcasts(): Single<List<Broadcast>> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single.just(createDebugListOfBroadcasts())
        }

        return getBroadcastsFromRemote()
    }

    override fun saveBroadcasts(broadcasts: List<Broadcast>): Completable {
        return Completable.create {
            it.onError(NotImplementedError())
        }
    }

    override fun getBroadcast(id: Long): Single<Broadcast> {
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests){
            return Single.just(createDebugBroadcast(withImage = true, isLive = true))
        }

        return getBroadcastFromRemote(id)
            .subscribeOn(ioScheduler)
    }

    private fun getBroadcastFromRemote(broadcastId: Long): Single<Broadcast>{
        return broadcastsApi
            .getBroadcast(broadcastId)
            .map { it.data ?: throw NoSuchDataInStorageException() }
            .map { stream ->
                stream.toBroadcast() ?: throw ReceivedDataWithWrongFormatException() }
            .subscribeOn(ioScheduler)
    }

    private fun getBroadcastsFromRemote(): Single<List<Broadcast>>{
        return broadcastsApi
            .getBroadcasts()
            .map{ it.data ?: throw NoSuchDataInStorageException() }
            .map{ streams -> streams.mapNotNull{ it.toBroadcast() } }
            .subscribeOn(ioScheduler)
    }

    private fun createDebugListOfBroadcasts(): List<Broadcast>{
        return listOf(
            createDebugBroadcast(withImage = true, isLive = true),
            createDebugBroadcast(withImage = true, isLive = false),
            createDebugBroadcast(withImage = false, isLive = true),
            createDebugBroadcast(withImage = false, isLive = false)
        )
    }

    private fun createDebugBroadcast(
        withImage: Boolean = false,
        isLive: Boolean = false
    ): Broadcast{
        val imageUrl = if(withImage) {
            "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg"
        } else ""

        val startsAt: DateTime
        val manifestUrl: String
        val viewersCount: Int?

        if(isLive){
            startsAt = DateTime.now().plusDays(2)
            manifestUrl = "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvUE8eBU1jbFJ-rvvSiAJVpToZhpXMn2x0vuMbhpnAN3ijq-X6mWxDLwLS-OblxDC-H8rGikJ1s8EtwOUwEZeSoMXPm7JpPMQdIbiWbd2ZWeuLIkLv_azK0V4UgFDSyrHde8IuyU9ZgY11kmQ1aH4soL1HxYpiJAkBtRUpI5bAZ2WNY_bR8QTyz6XCxhP5kCie75f2SfyRAbjkCQ9SRhwXipo2VrjijaBzwDogxXMZsZZzAiNBoEQi_WKzuJlQi1IN1jkorZL93uW6e7j-Bnt6MNCHlTu3MbcDwJ1MeNn3HKh-Xk1Y1ctlDoTTIg9cw1JIXzZ1q75e8hIeBa2VsSP-ZySE7bfY1X6T3eSPX53z2Sk6GAaHU3M6r2kmh639Q41x_EHNf9uPYnA2t-bFmeFwaifk-qslKsnBS4tqY8RXwQVTxHYQpC61UZWlnsfVi79E6jm78NWd0L4jMGngaVbiyJqly_qEeLQOIUntoXU4FsO4A891-6nDoKHhs4H8Jlb6U099ipbqdIOQxeZlcNv-oZbQJnT1KjbnAKY17F8QXVhDZQkYccB3ynPwVYdLTPmaviP9QQmo4UnEcwnNRrugaUXGOJj988hQ0HGtdyj9pwpLTFDMsx5f5ZJJdsThWo2MZLzwEmt-I1BqIlgqtmKBECaFgyBB7FSeHZ2YZq2uUwAWVca8HwowBFacyfdoQGYdryDpAn6Oh3BWTAQEZ71e8r51SleTyxEIERJGjN1AM8ptJnw8C4WZ2M8m_xbiqrSn8BycRt_L1Z_5qbo1V-4AJxqj8YwdZs5QnKRny_wfIHKmHQGDFSishj1-tiYjDJ_ATsD_hxJkASEO0jx4jVJqICjTE2gc5rhV8aDAcY9epL9ySi43qQDg.m3u8"
            viewersCount = 8
        } else{
            startsAt = DateTime.now().minusDays(2)
            manifestUrl = ""
            viewersCount = null
        }

        return Broadcast(
            0,
            "Отдай мне свои деньги 1",
            "Цыган вымогает деньги 1",
            startsAt,
            imageUrl,
            manifestUrl,
            viewersCount
        )
    }

    private fun Stream.toBroadcast(): Broadcast?{
        val broadcastId = id ?: return null
        val broadcastTitle = name ?: return null
        val broadcastDescription = description ?: return null
        val broadcastStartsAt = startAt ?: return null
        val broadcastImageUrl = image
        val broadcastManifestUrl = manifestUrl
        val broadcastViewersCount = null

        return Broadcast(
            broadcastId,
            broadcastTitle,
            broadcastDescription,
            broadcastStartsAt,
            broadcastImageUrl,
            broadcastManifestUrl,
            broadcastViewersCount
        )
    }
}