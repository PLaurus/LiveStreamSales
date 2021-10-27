package tv.wfc.livestreamsales.application.model.streamchatmessage

data class StreamChatMessageCreationResult(
    val isSent: Boolean,
    val errorMessage: String? = null
)