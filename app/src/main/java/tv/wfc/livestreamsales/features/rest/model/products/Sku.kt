package tv.wfc.livestreamsales.features.rest.model.products

import com.google.gson.annotations.SerializedName

data class Sku(
    @SerializedName("price")
    val price: Float?,
    @SerializedName("in_stock")
    val inStock: Int?
)