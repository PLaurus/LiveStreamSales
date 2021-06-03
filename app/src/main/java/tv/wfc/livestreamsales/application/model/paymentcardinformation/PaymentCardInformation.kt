package tv.wfc.livestreamsales.application.model.paymentcardinformation

data class PaymentCardInformation(
    /**
     * Made for simplicity. This is like to call [cardNumber] != null
     */
    val isBoundToAccount: Boolean,
    val cardNumber: String?
)