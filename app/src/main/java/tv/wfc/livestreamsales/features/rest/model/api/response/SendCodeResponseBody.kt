package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName

data class SendCodeResponseBody(
    @SerializedName("success")
    val isCodeSent: Boolean
)