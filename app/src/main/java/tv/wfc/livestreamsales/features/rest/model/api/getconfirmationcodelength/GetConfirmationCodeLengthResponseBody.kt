package tv.wfc.livestreamsales.features.rest.model.api.getconfirmationcodelength

import com.google.gson.annotations.SerializedName

data class GetConfirmationCodeLengthResponseBody(
    @SerializedName("code_length")
    val codeLength: Int
)