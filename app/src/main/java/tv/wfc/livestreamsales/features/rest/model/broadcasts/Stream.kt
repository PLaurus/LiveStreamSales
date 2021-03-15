package tv.wfc.livestreamsales.features.rest.model.broadcasts

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import tv.wfc.livestreamsales.features.rest.model.products.Product

data class Stream(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("start_at")
    val startAt: DateTime?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("products")
    val products: List<Product?>?
)