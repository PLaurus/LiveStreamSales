package tv.wfc.livestreamsales.application.datasource.mystream

import android.net.Uri
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.model.stream.MyStream

interface IMyStreamDataSource {
    /**
     * Creates new stream.
     * @param name stream name.
     * @param description stream description.
     * @param startAt date and time when stream comes online.
     * @param endAt date and time when stream goes offline.
     * @param image preview image of the stream.
     * @param videoWidth width of video.
     * @param videoHeight height of video.
     * @return created stream.
     */
    fun createMyStream(
        name: String,
        description: String,
        startAt: DateTime,
        endAt: DateTime,
        videoWidth: Int,
        videoHeight: Int,
        image: Uri? = null,
    ): Single<MyStream>

    /**
     * Updates stream.
     * @param streamId stream id.
     * @param name stream name.
     * @param description stream description.
     * @param startAt date and time when stream comes online.
     * @param endAt date and time when stream goes offline.
     * @param image preview image of the stream.
     * @param videoWidth width of video.
     * @param videoHeight height of video.
     * @return updated stream.
     */
    fun updateMyStream(
        streamId: Long,
        name: String,
        description: String,
        startAt: DateTime,
        endAt: DateTime,
        videoWidth: Int,
        videoHeight: Int,
        image: Uri? = null
    ): Single<MyStream>

    /**
     * Returns all streams owned by authorized user.
     * @return list of streams owned by authorized user.
     */
    fun getMyStreams(): Single<List<MyStream>>
}