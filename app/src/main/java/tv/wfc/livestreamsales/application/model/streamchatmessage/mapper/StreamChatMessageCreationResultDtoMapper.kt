package tv.wfc.livestreamsales.application.model.streamchatmessage.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessageCreationResult
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageCreationResultDto
import javax.inject.Inject

class StreamChatMessageCreationResultDtoMapper @Inject constructor(

): IEntityMapper<StreamChatMessageCreationResultDto, StreamChatMessageCreationResult> {
    override fun mapToDomainEntity(from: StreamChatMessageCreationResultDto): StreamChatMessageCreationResult? {
        return StreamChatMessageCreationResult(
            isSent = from.success ?: return null
        )
    }

    override fun mapToExternalEntity(from: StreamChatMessageCreationResult): StreamChatMessageCreationResultDto {
        return StreamChatMessageCreationResultDto(
            success = from.isSent
        )
    }
}