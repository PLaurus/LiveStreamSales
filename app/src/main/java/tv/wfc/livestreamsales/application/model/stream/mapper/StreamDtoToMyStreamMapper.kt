package tv.wfc.livestreamsales.application.model.stream.mapper

import org.joda.time.DateTime
import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamDto
import javax.inject.Inject

class StreamDtoToMyStreamMapper @Inject constructor(
    private val streamDtoToStreamingService: IEntityMapper<StreamDto, StreamingService>,
    private val iso8601StringToJodaDateTimeMapper: IEntityMapper<String, DateTime>
) : IEntityMapper<StreamDto, MyStream> {
    override fun map(from: StreamDto): MyStream? {
        val publicInfo = from.publicInfo ?: return null
        val streamingService = streamDtoToStreamingService.map(from) ?: return null

        val startsAtJodaDateTime =
            publicInfo.startAt?.let(iso8601StringToJodaDateTimeMapper::map) ?: return null

        val endsAtJodaDateTime =
            publicInfo.endAt?.let(iso8601StringToJodaDateTimeMapper::map) ?: return null

        return MyStream(
            id = publicInfo.id ?: return null,
            streamerId = publicInfo.userId ?: return null,
            wowzaId = from.wowzaId ?: return null,
            title = publicInfo.name ?: return null,
            description = publicInfo.description ?: return null,
            startsAt = startsAtJodaDateTime,
            endsAt = endsAtJodaDateTime,
            streamingService = streamingService,
            imageUrl = publicInfo.image,
            manifestUrl = publicInfo.manifest
        )
    }
}