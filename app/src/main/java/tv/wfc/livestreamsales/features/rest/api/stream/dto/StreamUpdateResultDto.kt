package tv.wfc.livestreamsales.features.rest.api.stream.dto

import com.google.gson.annotations.SerializedName

class StreamUpdateResultDto (
    @SerializedName("data")
    val data: StreamDto?
)