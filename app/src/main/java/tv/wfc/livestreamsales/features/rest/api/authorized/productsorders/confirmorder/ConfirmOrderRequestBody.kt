package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.confirmorder

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.models.Address

data class ConfirmOrderRequestBody(
    @SerializedName("delivery_date")
    val deliveryDate: DateTime?,
    @SerializedName("delivery_address")
    val deliveryAddress: Address?
){
    @SerializedName("_method")
    val method = "PATCH"
}