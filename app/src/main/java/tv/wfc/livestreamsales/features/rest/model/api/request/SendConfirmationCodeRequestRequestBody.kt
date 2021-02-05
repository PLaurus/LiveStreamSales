package tv.wfc.livestreamsales.features.rest.model.api.request

import com.google.gson.annotations.SerializedName

data class SendConfirmationCodeRequestRequestBody(
    @SerializedName("phone")
    val phoneNumber: String
)