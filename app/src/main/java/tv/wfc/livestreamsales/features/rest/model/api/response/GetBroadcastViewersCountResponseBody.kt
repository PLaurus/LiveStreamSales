package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastViewersCount

data class GetBroadcastViewersCountResponseBody(
    @SerializedName("data")
    val data: BroadcastViewersCount
)