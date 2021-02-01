package tv.wfc.livestreamsales.model.network.rest.response

import com.google.gson.annotations.SerializedName

data class GetNextCodeRequestRequiredWaitingTimeResponseBody(
    @SerializedName("waiting_time")
    val timeInSeconds: Long
)