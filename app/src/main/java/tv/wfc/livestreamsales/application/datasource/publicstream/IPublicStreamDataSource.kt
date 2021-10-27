package tv.wfc.livestreamsales.application.datasource.publicstream

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.stream.PublicStream

interface IPublicStreamDataSource {
    /**
     * Gets all public streams.
     * @return list of all public streams.
     */
    fun getAll(): Single<List<PublicStream>>

    /**
     * Saves provided [streams].
     * @return Completable which calls onSuccess when saving finished successfully
     * or onError when saving failed.
     */
    fun save(streams: List<PublicStream>): Completable

    /**
     * @param id stream id.
     * @return public stream with corresponding [id].
     */
    fun getById(id: Long): Single<PublicStream>

    /**
     * @param id stream id.
     * @return viewers count of stream with given [id].
     */
    fun getViewersCountByStreamId(id: Long): Single<Int>

    /**
     * @param stream public stream.
     * @return viewers count of given [stream].
     */
    fun getViewersCount(stream: PublicStream): Single<Int>
}