package tv.wfc.livestreamsales.model.network.rest.request

import com.google.gson.annotations.SerializedName

data class UpdateUserInformationRequestBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String?,
    @SerializedName("email")
    val email: String?
)