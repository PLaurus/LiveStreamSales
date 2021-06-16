package tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("skus")
    val skus: List<Sku>?
)