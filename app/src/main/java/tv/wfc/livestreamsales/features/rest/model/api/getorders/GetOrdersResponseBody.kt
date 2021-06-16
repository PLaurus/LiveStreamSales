package tv.wfc.livestreamsales.features.rest.model.api.getorders

import com.google.gson.annotations.SerializedName

data class GetOrdersResponseBody(
    @SerializedName("data")
    val orders: List<Order>?
)