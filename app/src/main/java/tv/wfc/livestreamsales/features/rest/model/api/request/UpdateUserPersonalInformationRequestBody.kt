package tv.wfc.livestreamsales.features.rest.model.api.request

import com.google.gson.annotations.SerializedName

data class UpdateUserPersonalInformationRequestBody(
    @SerializedName("first_name")
    val name: String?,
    @SerializedName("last_name")
    val surname: String?,
    @SerializedName("email")
    val email: String?
)