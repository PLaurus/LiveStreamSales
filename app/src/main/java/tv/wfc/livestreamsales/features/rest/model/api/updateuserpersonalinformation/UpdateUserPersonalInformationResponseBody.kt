package tv.wfc.livestreamsales.features.rest.model.api.updateuserpersonalinformation

import com.google.gson.annotations.SerializedName

data class UpdateUserPersonalInformationResponseBody(
    @SerializedName("success")
    val isInformationUpdatedSuccessfully: Boolean?
)