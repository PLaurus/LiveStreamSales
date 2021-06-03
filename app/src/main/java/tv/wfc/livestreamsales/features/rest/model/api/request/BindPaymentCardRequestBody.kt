package tv.wfc.livestreamsales.features.rest.model.api.request

import com.google.gson.annotations.SerializedName

data class BindPaymentCardRequestBody(
    @SerializedName("token")
    val paymentToken: String?
)