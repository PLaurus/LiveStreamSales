package com.example.livestreamsales.model.network.rest.error

import com.example.livestreamsales.model.network.rest.statuscode.HttpStatusCodeType

data class ResponseError(
    val httpStatusCodeType: HttpStatusCodeType,
    val message: String
)