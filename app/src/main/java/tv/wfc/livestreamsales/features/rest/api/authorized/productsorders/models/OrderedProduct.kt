package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.models

import com.google.gson.annotations.SerializedName

data class OrderedProduct(
    @SerializedName("product")
    val product: Product?,
    @SerializedName("amount")
    val amount: Int?
)