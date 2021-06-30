package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.models

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Float?,
    @SerializedName("image")
    val imageUrl: String?
)