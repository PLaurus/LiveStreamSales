package com.example.livestreamsales.model.network.rest.request

import com.google.gson.annotations.SerializedName

data class SendVerificationCodeRequestRequestBody(
    @SerializedName("phone")
    val telephoneNumber: String
)