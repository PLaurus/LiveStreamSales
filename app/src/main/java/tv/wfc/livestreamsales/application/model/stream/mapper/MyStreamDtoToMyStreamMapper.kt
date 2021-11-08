package tv.wfc.livestreamsales.application.model.stream.mapper

import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamDto
import javax.inject.Inject

class MyStreamDtoToMyStreamMapper @Inject constructor(
    private val myStreamDtoToStreamingServiceMapper: IEntityMapper<MyStreamDto, StreamingService>
) : IEntityMapper<MyStreamDto, MyStream> {
    override fun map(from: MyStreamDto): MyStream? {
        val publicInfo = from.publicInfo ?: return null
        val streamingService = myStreamDtoToStreamingServiceMapper.map(from) ?: return null

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