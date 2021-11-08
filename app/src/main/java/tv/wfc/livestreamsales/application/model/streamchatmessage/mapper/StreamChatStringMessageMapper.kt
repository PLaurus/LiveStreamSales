package tv.wfc.livestreamsales.application.model.streamchatmessage.mapper

import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import javax.inject.Inject

class StreamChatStringMessageMapper @Inject constructor(

): IEntityMapper<String, StreamChatMessage> {
    override fun map(from: String): StreamChatMessage {
        val sender = StreamChatMessage.Sender(
            id = -1,
            name = null,
            surname = null
        )

        return StreamChatMessage(sender, from)
    }
}