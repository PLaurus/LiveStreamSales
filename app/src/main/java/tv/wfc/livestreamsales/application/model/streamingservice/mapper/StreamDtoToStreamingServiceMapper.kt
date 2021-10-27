package tv.wfc.livestreamsales.application.model.streamingservice.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamDto
import javax.inject.Inject

class StreamDtoToStreamingServiceMapper @Inject constructor(
    private val streamDtoToStreamingServiceAuthenticationData: IEntityMapper<StreamDto, StreamingService.AuthenticationData>
) : IEntityMapper<StreamDto, StreamingService> {
    override fun map(from: StreamDto): StreamingService? =
        from.privateInfo?.run {
            val authenticationData = streamDtoToStreamingServiceAuthenticationData.map(from)

            StreamingService(
                serverAddress = primaryServer ?: return null,
                streamName = streamName ?: return null,
                serverPort = hostPort,
                authenticationData = authenticationData
            )
        }
}