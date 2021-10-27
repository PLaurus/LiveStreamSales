package tv.wfc.livestreamsales.features.rest.api.streamchat.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO used to create new chat message at server.
 */
class StreamChatMessageCreationDto (
    @SerializedName("message")
    val messageText: String?
)