package tv.wfc.livestreamsales.features.rest.model.api.confirmphonenumber

import com.google.gson.annotations.SerializedName

data class ConfirmPhoneNumberRequestBody(
    @SerializedName("phone")
    val phoneNumber: String,
    @SerializedName("confirm_code")
    val confirmationCode: Int
)