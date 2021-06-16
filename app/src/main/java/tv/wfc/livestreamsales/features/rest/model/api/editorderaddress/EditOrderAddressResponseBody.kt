package tv.wfc.livestreamsales.features.rest.model.api.editorderaddress

import com.google.gson.annotations.SerializedName

data class EditOrderAddressResponseBody(
    @SerializedName("success")
    val isOrderUpdated: Boolean?,
    @SerializedName("message")
    val message: String?
)