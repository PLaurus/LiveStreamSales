package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName

data class ConfirmPhoneNumberResponseBody(
    @SerializedName("success")
    val isPhoneNumberConfirmed: Boolean,
    @SerializedName("message")
    val errorMessage: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("profile_empty")
    val isProfileEmpty: Boolean?
)