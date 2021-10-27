package tv.wfc.livestreamsales.features.rest.api.mystreams.dto

import com.google.gson.annotations.SerializedName

data class MyStreamsDto(
    @SerializedName("data")
    val data: List<MyStreamDto?>?
)