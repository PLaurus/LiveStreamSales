package tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts

import com.google.gson.annotations.SerializedName

data class GetBroadcastResponseBody(
    @SerializedName("data")
    val data: Stream?
)