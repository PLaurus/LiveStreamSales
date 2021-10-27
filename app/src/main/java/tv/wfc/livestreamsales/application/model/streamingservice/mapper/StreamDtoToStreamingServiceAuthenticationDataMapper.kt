package tv.wfc.livestreamsales.application.model.streamingservice.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamDto
import javax.inject.Inject

class StreamDtoToStreamingServiceAuthenticationDataMapper @Inject constructor(

) : IEntityMapper<StreamDto, StreamingService.AuthenticationData> {
    override fun map(from: StreamDto): StreamingService.AuthenticationData? {
        return from.privateInfo?.run {
            if (userName != null && password != null) {
                StreamingService.AuthenticationData(userName, password)
            } else null
        }
    }
}