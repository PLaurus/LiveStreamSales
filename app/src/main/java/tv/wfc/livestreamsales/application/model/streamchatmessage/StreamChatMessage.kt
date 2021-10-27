package tv.wfc.livestreamsales.application.model.streamchatmessage

data class StreamChatMessage(
    val sender: Sender,
    val text: String
) {
    data class Sender(
        val id: Long,
        val name: String?,
        val surname: String?
    )
}