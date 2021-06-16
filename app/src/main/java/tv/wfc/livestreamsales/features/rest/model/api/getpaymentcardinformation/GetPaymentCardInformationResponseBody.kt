package tv.wfc.livestreamsales.features.rest.model.api.getpaymentcardinformation

import com.google.gson.annotations.SerializedName

data class GetPaymentCardInformationResponseBody(
    @SerializedName("data")
    val data: PaymentCardInformation?
)