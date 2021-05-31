package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName

data class OrderProductsResponseBody(
    @SerializedName("success")
    val areProductsOrdered: Boolean?
)