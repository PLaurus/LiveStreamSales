package tv.wfc.livestreamsales.model.network.rest.response

import com.google.gson.annotations.SerializedName

data class SendCodeResponseBody(
    @SerializedName("success")
    val isCodeSent: Boolean,
    @SerializedName("user_id")
    val userId: Int
)