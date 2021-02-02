package tv.wfc.livestreamsales.model.application.broadcastinformation

import com.google.gson.annotations.SerializedName

data class BroadcastBaseInformation(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val title: String,
    @SerializedName("starts_at")
    val startsAt: String?,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val imageUrl: String?
)