package com.example.livestreamsales.model.network.rest.response

import com.google.gson.annotations.SerializedName

data class UpdateUserInformationResponseBody(
    @SerializedName("success")
    val isInformationUpdatedSuccessfully: Boolean
)