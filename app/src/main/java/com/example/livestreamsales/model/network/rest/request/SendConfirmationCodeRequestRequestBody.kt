package com.example.livestreamsales.model.network.rest.request

import com.google.gson.annotations.SerializedName

data class SendConfirmationCodeRequestRequestBody(
    @SerializedName("phone")
    val phoneNumber: String
)