package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.features.rest.model.userpersonalinformation.UserPersonalInformation

data class GetUserPersonalInformationResponseBody(
    @SerializedName("data")
    val data: UserPersonalInformation?
)