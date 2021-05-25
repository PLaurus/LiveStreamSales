package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName

data class LinkPaymentCardResponseBody(
    @SerializedName("success")
    val isPaymentCardLinked: Boolean?
)