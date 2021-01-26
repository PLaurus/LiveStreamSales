package com.example.livestreamsales.model.network.rest.response

import com.google.gson.annotations.SerializedName

data class GetUserInformationResponseBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String?,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String?
)