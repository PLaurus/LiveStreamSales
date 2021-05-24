package tv.wfc.livestreamsales.features.rest.model.userpersonalinformation

import com.google.gson.annotations.SerializedName

data class UserPersonalInformation(
    @SerializedName("first_name")
    val name: String?,
    @SerializedName("last_name")
    val surname: String?,
    @SerializedName("phone")
    val phoneNumber: String?,
    @SerializedName("email")
    val email: String?
)