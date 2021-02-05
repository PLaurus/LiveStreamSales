package tv.wfc.livestreamsales.features.rest.model.api.request

import com.google.gson.annotations.SerializedName

data class UpdateUserInformationRequestBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String?,
    @SerializedName("email")
    val email: String?
)