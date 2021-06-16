package tv.wfc.livestreamsales.features.rest.model.api.orderproducts

import com.google.gson.annotations.SerializedName

data class OrderProductsRequestBody(
    @SerializedName("sku")
    val products: List<Product>
)