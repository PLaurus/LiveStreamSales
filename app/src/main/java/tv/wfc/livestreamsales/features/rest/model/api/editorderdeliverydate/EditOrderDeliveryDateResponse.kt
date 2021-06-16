package tv.wfc.livestreamsales.features.rest.model.api.editorderdeliverydate

import com.google.gson.annotations.SerializedName

data class EditOrderDeliveryDateResponse(
    @SerializedName("success")
    val isOrderUpdated: Boolean?,
    @SerializedName("message")
    val message: String?
)