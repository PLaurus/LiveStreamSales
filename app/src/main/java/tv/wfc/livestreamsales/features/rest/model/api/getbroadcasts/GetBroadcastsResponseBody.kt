package tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts

import com.google.gson.annotations.SerializedName

data class GetBroadcastsResponseBody(
    @SerializedName("data")
    val data: List<Stream>?
)