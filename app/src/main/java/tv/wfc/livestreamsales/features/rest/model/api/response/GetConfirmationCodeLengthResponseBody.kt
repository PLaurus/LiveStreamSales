package tv.wfc.livestreamsales.features.rest.model.api.response

import com.google.gson.annotations.SerializedName

data class GetConfirmationCodeLengthResponseBody(
    @SerializedName("code_length")
    val codeLength: Int
)