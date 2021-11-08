package tv.wfc.livestreamsales.application.model.streamingservice.mapper

import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamDto
import javax.inject.Inject

class MyStreamDtoToStreamingServiceMapper @Inject constructor(
    private val myStreamDtoToStreamingServiceAuthenticationData: IEntityMapper<MyStreamDto, StreamingService.AuthenticationData>
): IEntityMapper<MyStreamDto, StreamingService> {
    override fun map(from: MyStreamDto): StreamingService? {
        return from.privateInfo?.run{
            val authenticationData = myStreamDtoToStreamingServiceAuthenticationData.map(from)

            StreamingService(
                serverAddress = primaryServer ?: return null,
                streamName = streamName ?: return null,
                serverPort = hostPort,
                authenticationData
            )
        }
    }
}