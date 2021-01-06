package com.example.livestreamsales.model.network.rest.response

import com.google.gson.annotations.SerializedName

data class ConfirmTelephoneNumberResponseBody(
    @SerializedName("is_code_valid")
    val isCodeValid: Boolean,
    @SerializedName("does_user_must_register")
    val doesUserMustRegister: Boolean?,
    @SerializedName("token")
    val token: String?
)