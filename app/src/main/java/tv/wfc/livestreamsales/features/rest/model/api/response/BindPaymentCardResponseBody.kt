package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName

data class BindPaymentCardResponseBody(
    /**
     * If false [errorMessage] will explain the reason.
     */
    @SerializedName("success")
    val isBindingFlowStarted: Boolean?,

    /**
     * Explains why payment card was not bound to account.
     * Usually contains transaction cancellation cause taken from YooKassa
     */
    @SerializedName("message")
    val errorMessage: String?,

    /**
     * Link for payment confirmation.
     * For example can be sent to 3dSecure Intent developed by YooKassa
     */
    @SerializedName("confirmation_url")
    val confirmationUrl: String?
)