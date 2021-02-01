package tv.wfc.livestreamsales.model.network.rest.response

import com.google.gson.annotations.SerializedName

data class ConfirmPhoneNumberResponseBody(
    @SerializedName("success")
    val isPhoneNumberConfirmed: Boolean,
    @SerializedName("message")
    val errorMessage: String?,
    @SerializedName("token")
    val token: String?
)