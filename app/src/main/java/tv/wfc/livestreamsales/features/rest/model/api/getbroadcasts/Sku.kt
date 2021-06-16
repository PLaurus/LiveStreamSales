package tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts

import com.google.gson.annotations.SerializedName

data class Sku(
    @SerializedName("id")
    val id: Long,
    @SerializedName("in_stock")
    val inStock: Int?,
    @SerializedName("price")
    val price: Float?,
    @SerializedName("properties")
    val properties: List<Property>?
)