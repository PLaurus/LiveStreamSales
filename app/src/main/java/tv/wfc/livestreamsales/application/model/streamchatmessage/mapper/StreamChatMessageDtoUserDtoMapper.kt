package tv.wfc.livestreamsales.application.model.streamchatmessage.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageDto
import javax.inject.Inject

class StreamChatMessageDtoUserDtoMapper @Inject constructor(

): IEntityMapper<StreamChatMessageDto.UserDto, StreamChatMessage.Sender> {
    override fun mapToDomainEntity(from: StreamChatMessageDto.UserDto): StreamChatMessage.Sender? {
        val id = from.id ?: return null

        return StreamChatMessage.Sender(
            id = id,
            name = from.name,
            surname = from.surname
        )
    }

    override fun mapToExternalEntity(from: StreamChatMessage.Sender): StreamChatMessageDto.UserDto {
        return StreamChatMessageDto.UserDto(
            id = from.id,
            name = from.name,
            surname = from.surname
        )
    }
}