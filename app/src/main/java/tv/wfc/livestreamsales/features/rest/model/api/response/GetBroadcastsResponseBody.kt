package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.features.rest.model.broadcasts.Stream

data class GetBroadcastsResponseBody(
    @SerializedName("data")
    val data: List<Stream>?
)