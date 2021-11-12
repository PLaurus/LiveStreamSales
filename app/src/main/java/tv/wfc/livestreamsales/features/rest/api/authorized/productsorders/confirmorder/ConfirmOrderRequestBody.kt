package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.confirmorder

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.models.Address

/**
 * Request body for order confirmation.
 * @param deliveryDate represents delivery date as string based on the ISO8601 standard.
 * @param deliveryAddress delivery address.
 */
data class ConfirmOrderRequestBody(
    /**
     * Date and time when the order will be delivered to the customer.
     * Date and time is represented as string based on ISO8601 standard.
     */
    @SerializedName("delivery_date")
    val deliveryDate: String?,
    /**
     * Delivery address.
     */
    @SerializedName("delivery_address")
    val deliveryAddress: Address?
){
    @SerializedName("_method")
    val method = "PATCH"
}