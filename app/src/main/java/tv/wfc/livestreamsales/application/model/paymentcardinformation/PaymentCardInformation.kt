package tv.wfc.livestreamsales.application.model.paymentcardinformation

data class PaymentCardInformation(
    /**
     * Made for simplicity. This is like to call [cardNumber] != null
     */
    val isBoundToAccount: Boolean,
    val cardNumber: String? = null,
    val bindingState: BindingState? = null
){
    enum class BindingState{
        PENDING,
        WAITING_FOR_CAPTURE,
        SUCCEEDED,
        CANCELED
    }
}