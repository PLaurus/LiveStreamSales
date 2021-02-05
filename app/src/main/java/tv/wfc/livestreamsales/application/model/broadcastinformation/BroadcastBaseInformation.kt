package tv.wfc.livestreamsales.application.model.broadcastinformation

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class BroadcastBaseInformation(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("starts_at")
    val startsAt: DateTime? = null,
    @SerializedName("image")
    val imageUrl: String? = null
)