package tv.wfc.livestreamsales.features.rest.model.api.getorders

import com.google.gson.annotations.SerializedName

data class OrderedProduct(
    @SerializedName("product")
    val product: Product?,
    @SerializedName("amount")
    val amount: Int?
)