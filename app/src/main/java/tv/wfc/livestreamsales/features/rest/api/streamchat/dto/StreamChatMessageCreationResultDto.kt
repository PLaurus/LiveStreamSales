package tv.wfc.livestreamsales.features.rest.api.streamchat.dto

import com.google.gson.annotations.SerializedName

data class StreamChatMessageCreationResultDto (
    /**
     * True - message was successfully created on server;
     * False - error occurred while message creation.
     */
    @SerializedName("success")
    val success: Boolean?
)