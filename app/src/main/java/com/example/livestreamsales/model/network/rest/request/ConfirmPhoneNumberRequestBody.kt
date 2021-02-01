package com.example.livestreamsales.model.network.rest.request

import com.google.gson.annotations.SerializedName

data class ConfirmPhoneNumberRequestBody(
    @SerializedName("phone")
    val phoneNumber: String,
    @SerializedName("confirm_code")
    val confirmationCode: Int
)