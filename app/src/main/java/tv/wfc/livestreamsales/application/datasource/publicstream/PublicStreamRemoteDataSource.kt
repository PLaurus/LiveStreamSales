package tv.wfc.livestreamsales.application.datasource.publicstream

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.exception.storage.NoSuchDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.model.stream.PublicStream
import tv.wfc.livestreamsales.features.rest.api.notauthorized.IBroadcastsApi
import tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts.Stream
import javax.inject.Inject

class PublicStreamRemoteDataSource @Inject constructor(
    private val broadcastsApi: IBroadcastsApi,
    private val iso8601StringToJodaDateTimeMapper: IEntityMapper<String, DateTime>,
    @IoScheduler
    private val ioScheduler: Scheduler
) : IPublicStreamDataSource {
    override fun getAll(): Single<List<PublicStream>> {
        if (BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests) {
            return Single.just(createDebugListOfBroadcasts())
        }

        return getBroadcastsFromRemote()
    }

    override fun save(streams: List<PublicStream>): Completable {
        return Completable.create {
            it.onError(NotImplementedError())
        }
    }

    override fun getById(id: Long): Single<PublicStream> {
        if (BuildConfig.DEBUG && BuildConfig.IgnoreRestRequests) {
            return Single.just(createDebugBroadcast(withImage = true, isLive = true))
        }

        return getBroadcastFromRemote(id)
            .subscribeOn(ioScheduler)
    }

    override fun getViewersCountByStreamId(id: Long): Single<Int> {
        return broadcastsApi
            .getBroadcastViewersCount(id)
            .subscribeOn(ioScheduler)
    }

    override fun getViewersCount(stream: PublicStream): Single<Int> {
        val streamId = stream.id

        return getViewersCountByStreamId(streamId)
    }

    private fun getBroadcastFromRemote(broadcastId: Long): Single<PublicStream> {
        return broadcastsApi
            .getBroadcast(broadcastId)
            .map { it.data ?: throw NoSuchDataInStorageException() }
            .map { stream ->
                stream.toBroadcast() ?: throw ReceivedDataWithWrongFormatException()
            }
            .subscribeOn(ioScheduler)
    }

    private fun getBroadcastsFromRemote(): Single<List<PublicStream>> {
        return broadcastsApi
            .getBroadcasts()
            .map { it.data ?: throw NoSuchDataInStorageException() }
            .map { streams -> streams.mapNotNull { it.toBroadcast() } }
            .subscribeOn(ioScheduler)
    }

    private fun createDebugListOfBroadcasts(): List<PublicStream> {
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
    ): PublicStream {
        val imageUrl = if (withImage) {
            "https://www.creativefabrica.com/wp-content/uploads/2019/03/Monogram-YA-Logo-Design-by-Greenlines-Studios.jpg"
        } else ""

        val startsAt: DateTime
        val manifestUrl: String

        if (isLive) {
            startsAt = DateTime.now().plusDays(2)
            manifestUrl =
                "https://video-weaver.hel01.hls.ttvnw.net/v1/playlist/CvUE8eBU1jbFJ-rvvSiAJVpToZhpXMn2x0vuMbhpnAN3ijq-X6mWxDLwLS-OblxDC-H8rGikJ1s8EtwOUwEZeSoMXPm7JpPMQdIbiWbd2ZWeuLIkLv_azK0V4UgFDSyrHde8IuyU9ZgY11kmQ1aH4soL1HxYpiJAkBtRUpI5bAZ2WNY_bR8QTyz6XCxhP5kCie75f2SfyRAbjkCQ9SRhwXipo2VrjijaBzwDogxXMZsZZzAiNBoEQi_WKzuJlQi1IN1jkorZL93uW6e7j-Bnt6MNCHlTu3MbcDwJ1MeNn3HKh-Xk1Y1ctlDoTTIg9cw1JIXzZ1q75e8hIeBa2VsSP-ZySE7bfY1X6T3eSPX53z2Sk6GAaHU3M6r2kmh639Q41x_EHNf9uPYnA2t-bFmeFwaifk-qslKsnBS4tqY8RXwQVTxHYQpC61UZWlnsfVi79E6jm78NWd0L4jMGngaVbiyJqly_qEeLQOIUntoXU4FsO4A891-6nDoKHhs4H8Jlb6U099ipbqdIOQxeZlcNv-oZbQJnT1KjbnAKY17F8QXVhDZQkYccB3ynPwVYdLTPmaviP9QQmo4UnEcwnNRrugaUXGOJj988hQ0HGtdyj9pwpLTFDMsx5f5ZJJdsThWo2MZLzwEmt-I1BqIlgqtmKBECaFgyBB7FSeHZ2YZq2uUwAWVca8HwowBFacyfdoQGYdryDpAn6Oh3BWTAQEZ71e8r51SleTyxEIERJGjN1AM8ptJnw8C4WZ2M8m_xbiqrSn8BycRt_L1Z_5qbo1V-4AJxqj8YwdZs5QnKRny_wfIHKmHQGDFSishj1-tiYjDJ_ATsD_hxJkASEO0jx4jVJqICjTE2gc5rhV8aDAcY9epL9ySi43qQDg.m3u8"
        } else {
            startsAt = DateTime.now().minusDays(2)
            manifestUrl = ""
        }

        val endsAt: DateTime = startsAt.plusHours(1)

        return PublicStream(
            id = 0,
            streamerId = 0,
            title = "The title is here",
            description = "The description is here",
            startsAt,
            endsAt,
            imageUrl,
            manifestUrl
        )
    }

    private fun Stream.toBroadcast(): PublicStream? {
        val startsAtJodaDateTime =
            startAt?.let(iso8601StringToJodaDateTimeMapper::map) ?: return null

        val endsAtJodaDateTime =
            endsAt?.let(iso8601StringToJodaDateTimeMapper::map) ?: return null

        return PublicStream(
            id = id ?: return null,
            streamerId = userId ?: return null,
            title = name ?: return null,
            description = description ?: return null,
            startsAt = startsAtJodaDateTime,
            endsAt = endsAtJodaDateTime,
            imageUrl = image,
            manifestUrl = manifestUrl
        )
    }
}