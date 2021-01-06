package com.example.livestreamsales.model.network.rest.request

import com.google.gson.annotations.SerializedName

data class SendVerificationCodeRequestRequestBody(
    @SerializedName("telephone_number")
    val telephoneNumber: String
)