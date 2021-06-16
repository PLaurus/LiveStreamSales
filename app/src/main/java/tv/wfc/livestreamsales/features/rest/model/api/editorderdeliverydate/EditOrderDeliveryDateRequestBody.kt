package tv.wfc.livestreamsales.features.rest.model.api.editorderdeliverydate

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class EditOrderDeliveryDateRequestBody(
    @SerializedName("delivery_date")
    val deliveryDate: DateTime?
){
    @SerializedName("_method")
    val method = "PATCH"
}