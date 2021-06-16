package tv.wfc.livestreamsales.features.rest.model.api.getuserpersonalinformation

import com.google.gson.annotations.SerializedName

data class GetUserPersonalInformationResponseBody(
    @SerializedName("data")
    val data: UserPersonalInformation?
)