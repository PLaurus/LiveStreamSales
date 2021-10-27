package tv.wfc.livestreamsales.features.rest.api.stream.dto

import com.google.gson.annotations.SerializedName

data class StreamCreationResultDto(
    @SerializedName("data")
    val data: StreamDto?
)