package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName
import tv.wfc.livestreamsales.features.rest.model.paymentcardinformation.PaymentCardInformation

data class GetPaymentCardInformationResponseBody(
    @SerializedName("data")
    val data: PaymentCardInformation?
)