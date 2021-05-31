package tv.wfc.livestreamsales.features.rest.model.api.request

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.features.rest.model.productsorder.Product

data class OrderProductsRequestBody(
    @SerializedName("sku")
    val products: List<Product>
)