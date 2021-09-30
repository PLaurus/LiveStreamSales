package tv.wfc.livestreamsales.features.rest.api.streamchat.dto

import com.google.gson.annotations.SerializedName

data class StreamChatMessageDto (
    @SerializedName("message")
    val message: String?,
    @SerializedName("streamId")
    val streamId: Long?,
    @SerializedName("user")
    val user: UserDto?
) {
    data class UserDto(
        @SerializedName("id")
        val id: Long?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("surname")
        val surname: String?
    )
}