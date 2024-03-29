package tv.wfc.livestreamsales.features.rest.model.api.getpaymentcardinformation

import com.google.gson.annotations.SerializedName

data class PaymentCardInformation(
    @SerializedName("card_number")
    val cardNumber: String?,

    /**
     * Made for simplicity. This is like to call [cardNumber] != null
     */
    @SerializedName("is_binding")
    val isBoundToAccount: Boolean?,

    @SerializedName("binding_status")
    val bindingState: String? // pending, waiting_for_capture, succeeded and canceled
)