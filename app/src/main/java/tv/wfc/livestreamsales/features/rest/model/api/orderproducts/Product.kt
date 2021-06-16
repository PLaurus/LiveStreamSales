package tv.wfc.livestreamsales.features.rest.model.api.orderproducts

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val productId: Long?,
    @SerializedName("quantity")
    val quantity: Int?
)