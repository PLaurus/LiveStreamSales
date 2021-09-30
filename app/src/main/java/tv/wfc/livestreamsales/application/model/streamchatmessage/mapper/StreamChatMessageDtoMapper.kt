package tv.wfc.livestreamsales.application.model.streamchatmessage.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageDto
import javax.inject.Inject

class StreamChatMessageDtoMapper @Inject constructor(
    private val userDtoMapper: IEntityMapper<StreamChatMessageDto.UserDto, StreamChatMessage.Sender>
): IEntityMapper<StreamChatMessageDto, StreamChatMessage> {
    override fun mapToDomainEntity(from: StreamChatMessageDto): StreamChatMessage? {
        val remoteSender = from.user ?: return null
        val sender = userDtoMapper.mapToDomainEntity(remoteSender) ?: return null
        val text = from.message ?: return null

        return StreamChatMessage(sender, text)
    }

    override fun mapToExternalEntity(from: StreamChatMessage): StreamChatMessageDto {
        val user = userDtoMapper.mapToExternalEntity(from.sender)

        return StreamChatMessageDto(
            from.text,
            streamId = null,
            user
        )
    }
}