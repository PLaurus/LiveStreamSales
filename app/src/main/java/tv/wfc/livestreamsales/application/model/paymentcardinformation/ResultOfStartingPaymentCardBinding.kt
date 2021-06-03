package tv.wfc.livestreamsales.application.model.paymentcardinformation

data class ResultOfStartingPaymentCardBinding(
    /**
     * If false, [errorMessage] will explain the reason.
     */
    val isBindingFlowStarted: Boolean,

    /**
     * Explains why payment card was not bound to account.
     * Usually contains transaction cancellation cause taken from YooKassa
     */
    val errorMessage: String? = null,

    /**
     * Link for payment confirmation.
     * For example can be sent to 3dSecure Intent developed by YooKassa
     */
    val confirmationUrl: String? = null
)