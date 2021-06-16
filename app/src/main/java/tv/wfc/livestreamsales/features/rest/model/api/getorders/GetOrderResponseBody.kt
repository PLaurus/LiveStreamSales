package tv.wfc.livestreamsales.features.rest.model.api.getorders

import com.google.gson.annotations.SerializedName

data class GetOrderResponseBody(
    @SerializedName("data")
    val order: Order?
)