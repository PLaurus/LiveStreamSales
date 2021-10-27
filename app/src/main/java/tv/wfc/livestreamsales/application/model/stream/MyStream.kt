package tv.wfc.livestreamsales.application.model.stream

import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService

/**
 * Contains full information about stream intended for stream creator.
 * Private information like [streamingService] can be used for launching
 * the stream by stream creator.
 */
data class MyStream (
    override val id: Long,
    override val streamerId: Long,
    /**
     * Unique identifier of the stream that is stored at wowza streaming service.
     */
    val wowzaId: String,
    override val title: String,
    override val description: String,
    override val startsAt: DateTime,
    override val endsAt: DateTime,
    /**
     * Streaming service information that must be used to start stream.
     */
    val streamingService: StreamingService,
    override val imageUrl: String? = null,
    override val manifestUrl: String? = null
): IStream