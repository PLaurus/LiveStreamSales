package tv.wfc.livestreamsales.application.model.stream.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamDto
import javax.inject.Inject

class StreamDtoToMyStreamMapper @Inject constructor(
    private val streamDtoToStreamingService: IEntityMapper<StreamDto, StreamingService>
) : IEntityMapper<StreamDto, MyStream> {
    override fun map(from: StreamDto): MyStream? {
        val publicInfo = from.publicInfo ?: return null
        val streamingService = streamDtoToStreamingService.map(from) ?: return null

        return MyStream(
            id = publicInfo.id ?: return null,
            streamerId = from.userId ?: return null,
            wowzaId = from.wowzaId ?: return null,
            title = publicInfo.name ?: return null,
            description = publicInfo.description ?: return null,
            startsAt = publicInfo.startAt ?: return null,
            endsAt = publicInfo.endAt ?: return null,
            streamingService = streamingService,
            imageUrl = publicInfo.image,
            manifestUrl = publicInfo.manifest
        )
    }
}