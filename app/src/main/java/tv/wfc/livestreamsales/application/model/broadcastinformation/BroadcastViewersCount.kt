package tv.wfc.livestreamsales.application.model.broadcastinformation

import com.google.gson.annotations.SerializedName

data class BroadcastViewersCount(
    @SerializedName("viewers_count")
    val viewersCount: Int
)