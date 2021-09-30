package tv.wfc.livestreamsales.application.model.streamchatmessage.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import javax.inject.Inject

class StreamChatStringMessageMapper @Inject constructor(

): IEntityMapper<String, StreamChatMessage> {
    override fun mapToDomainEntity(from: String): StreamChatMessage {
        val sender = StreamChatMessage.Sender(
            id = -1,
            name = null,
            surname = null
        )

        return StreamChatMessage(sender, from)
    }

    override fun mapToExternalEntity(from: StreamChatMessage): String {
        return from.text
    }
}