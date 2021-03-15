package tv.wfc.livestreamsales.application.storage.broadcastsinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast
import tv.wfc.livestreamsales.application.model.exceptions.DataNotReceivedException
import tv.wfc.livestreamsales.application.model.exceptions.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.storage.broadcastsinformation.IBroadcastsStorage
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsApi
import tv.wfc.livestreamsales.features.rest.model.broadcasts.Stream
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
            .map { it.data ?: throw DataNotReceivedException() }
            .map { stream ->
                stream.toBroadcast() ?: throw ReceivedDataWithWrongFormatException() }
            .subscribeOn(ioScheduler)
    }

    private fun getBroadcastsFromRemote(): Single<List<Broadcast>>{
        return broadcastsApi
            .getBroadcasts()
            .map{ it.data ?: throw DataNotReceivedException() }
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
            manifestUrl = "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvEEjOHvHQ5GZbiwDNwl1cAXztXeE4zbW9yCbhupYhEWV7abJ4YzK2lVSZvYSEuCzF0Ibw0uL2FeNh-nGS8aIDd3mwrPsmfUofV_9o8p615hHAyHrR2sVk6fYvCctUS3qt7BMKt_YlfZz6qBgoOVRJQzmkFjbonaMTpxYBuu_sB27ZRmGXajfHVObrHy4qcg83h7tXy-ojFMi7k1dZNoVkfvf_zaT5dFT1dB4sD_NhsBPs2Ys4XPqhq8c7kGFKhx0MUIsDi59uqK0XVg_WjUSUERilt5an4yvVciHsPgBSax_So2xjUF-naiI8bIuN48MxgHI6ulzlRuWBXDhRFD9N_BZ4jAaw4noZBD90__crjfdZSLZFceaiPm2KqL3opSgjpb8Ae04fhAHT4SwjAZ0LWFCMh9E03WpTq-qjCik-aQMFghxUQNMLUY3un2X3uEbD2ZMbZMd_HAOGSKDBSnVIJPQasgmt3X35fbWH_hjS6ZpVOjY7f__hpsXalFsKSRliq-4AtE5jqUvYccFFrhwYCf50m897uJDKZNQrJZSei5fVYr4uVr0cTWt2vQG8TrO8MdjEP_RWjTQpj9v54kxxDLcPnMxmVmGF3qniS_HJjCyFWdrsksvsDr8zq-Q4DN4-krORblTzT1XcugpyCV5a6s7Va95oA2glAUo2NtKDiEBb5HiWWpZ6sRk6cBvlKzmCLeeuPQo2a2LSS4cV3EJvsdr_gEBRWhTrpFeGYS0jTyPkQ--tN8q-T6GKXaz581grnY231jwS-7mJ2Q9cGYue-vGPt2mIP36_5oYUU9CJgUvtDUW6locgdO9TObLGukUbCJ_RIQnvPYC_MW4Uct1NSx-g7iJhoMVoaQJ1yc3DbFxL7P.m3u8"
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
        val broadcastManifestUrl = null
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