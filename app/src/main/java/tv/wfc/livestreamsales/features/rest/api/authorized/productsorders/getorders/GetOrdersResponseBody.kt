package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.getorders

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.models.Order

data class GetOrdersResponseBody(
    @SerializedName("data")
    val orders: List<Order>?
)