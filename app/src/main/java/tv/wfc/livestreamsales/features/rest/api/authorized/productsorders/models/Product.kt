package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.models

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.features.rest.model.api.orderproducts.Property

data class Product(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Float?,
    @SerializedName("image")
    val imageUrl: String?,
    @SerializedName("properties")
    val properties: List<Property>?
)