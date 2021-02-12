package tv.wfc.livestreamsales.application.model.broadcastinformation

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class BroadcastInformation(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("start_at")
    val startsAt: DateTime? = null,
    @SerializedName("image")
    val imageUrl: String? = null,
    @SerializedName("manifest")
    val manifestUrl: String? = null,
    @SerializedName("viewers_count")
    val viewersCount: Int? = null
)