package tv.wfc.livestreamsales.features.rest.model.api.getproducts

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts.Product

data class GetProductsResponseBody(
    @SerializedName("data")
    val data: List<Product>
)