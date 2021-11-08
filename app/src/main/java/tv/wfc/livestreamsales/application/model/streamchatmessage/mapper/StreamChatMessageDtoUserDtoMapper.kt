package tv.wfc.livestreamsales.application.model.streamchatmessage.mapper

import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageDto
import javax.inject.Inject

class StreamChatMessageDtoUserDtoMapper @Inject constructor(

): IEntityMapper<StreamChatMessageDto.UserDto, StreamChatMessage.Sender> {
    override fun map(from: StreamChatMessageDto.UserDto): StreamChatMessage.Sender? {
        val id = from.id ?: return null

        return StreamChatMessage.Sender(
            id = id,
            name = from.name,
            surname = from.surname
        )
    }
}