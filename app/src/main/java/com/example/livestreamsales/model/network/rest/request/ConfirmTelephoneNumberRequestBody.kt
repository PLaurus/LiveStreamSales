package com.example.livestreamsales.model.network.rest.request

import com.google.gson.annotations.SerializedName

data class ConfirmTelephoneNumberRequestBody(
    @SerializedName("verification_code")
    val verificationCode: Int
)