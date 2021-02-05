package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName

data class GetUserInformationResponseBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String?,
    @SerializedName("phone")
    val phoneNumber: String,
    @SerializedName("email")
    val email: String?
)