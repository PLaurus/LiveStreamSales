package tv.wfc.livestreamsales.features.rest.model.api.request

import com.google.gson.annotations.SerializedName

data class NotifyWatchingBroadcastRequestBody(
    @SerializedName("broadcast_id")
    val broadcastId: Long
)