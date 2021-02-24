package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.application.model.Product

data class GetProductsResponseBody(
    @SerializedName("data")
    val data: List<Product>
)