package com.example.livestreamsales.model.request

import com.google.gson.annotations.SerializedName

data class SendCodeRequestBody(
    @SerializedName("telephone_number")
    val telephoneNumber: String
)