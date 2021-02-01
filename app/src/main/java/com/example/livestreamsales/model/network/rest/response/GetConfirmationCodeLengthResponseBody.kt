package com.example.livestreamsales.model.network.rest.response

import com.google.gson.annotations.SerializedName

data class GetConfirmationCodeLengthResponseBody(
    @SerializedName("code_length")
    val codeLength: Int
)