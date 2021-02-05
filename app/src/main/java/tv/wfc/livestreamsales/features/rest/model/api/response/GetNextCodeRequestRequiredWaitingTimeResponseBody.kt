package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName

data class GetNextCodeRequestRequiredWaitingTimeResponseBody(
    @SerializedName("waiting_time")
    val timeInSeconds: Long
)