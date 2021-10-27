package tv.wfc.livestreamsales.application.model.stream

import org.joda.time.DateTime

interface IStream {
    /**
     * Unique identifier of a stream.
     */
    val id: Long

    /**
     * Unique identifier of the user who created the stream.
     */
    val streamerId: Long

    /**
     * Stream title.
     */
    val title: String

    /**
     * Stream description.
     */
    val description: String

    /**
     * Date and time when stream will be started.
     */
    val startsAt: DateTime

    /**
     * Date and time when stream will be finished.
     */
    val endsAt: DateTime

    /**
     * Url of stream preview image.
     */
    val imageUrl: String?

    /**
     * Url of HLS manifest that contains video associated with the stream.
     */
    val manifestUrl: String?
}