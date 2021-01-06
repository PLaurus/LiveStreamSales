package com.example.livestreamsales.model.network.rest.response

import com.google.gson.annotations.SerializedName

data class SendCodeResponseBody(
    @SerializedName("is_code_sent")
    val isCodeSent: Boolean
)