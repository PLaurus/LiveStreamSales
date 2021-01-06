package com.example.livestreamsales.network.rest.errors

import com.example.livestreamsales.model.network.rest.error.ResponseError
import io.reactivex.rxjava3.core.Observable

interface IResponseErrorsManager {
    val errors: Observable<ResponseError>
    fun checkResponseStatus(statusCode: Int, message: String)
}