package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.confirmorder

import com.google.gson.annotations.SerializedName

data class ConfirmOrderResponseBody(
    @SerializedName("success")
    val isOrderConfirmed: Boolean?,
    @SerializedName("message")
    val message: String?
)