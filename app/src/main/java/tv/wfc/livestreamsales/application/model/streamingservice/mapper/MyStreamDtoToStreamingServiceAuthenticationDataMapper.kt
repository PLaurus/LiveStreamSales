package tv.wfc.livestreamsales.application.model.streamingservice.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamDto
import javax.inject.Inject

class MyStreamDtoToStreamingServiceAuthenticationDataMapper @Inject constructor(

) : IEntityMapper<MyStreamDto, StreamingService.AuthenticationData>{
    override fun map(from: MyStreamDto): StreamingService.AuthenticationData? {
        return from.privateInfo?.run {
            if (userName != null && password != null) {
                StreamingService.AuthenticationData(userName, password)
            } else null
        }
    }
}