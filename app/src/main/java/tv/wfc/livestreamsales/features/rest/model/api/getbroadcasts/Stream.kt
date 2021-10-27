package tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class Stream(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("user_id")
    val userId: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("start_at")
    val startAt: DateTime?,
    @SerializedName("end_at")
    val endsAt: DateTime?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("manifest")
    val manifestUrl: String?,
    @SerializedName("products")
    val products: List<Product?>?
)