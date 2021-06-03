package tv.wfc.livestreamsales.features.rest.model.paymentcardinformation

import com.google.gson.annotations.SerializedName

data class PaymentCardInformation(
    @SerializedName("card_number")
    val cardNumber: String?,

    /**
     * Made for simplicity. This is like to call [cardNumber] != null
     */
    @SerializedName("is_binding")
    val isBoundToAccount: Boolean?
)