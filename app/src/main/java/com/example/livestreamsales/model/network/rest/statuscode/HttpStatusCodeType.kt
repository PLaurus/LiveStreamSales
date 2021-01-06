package com.example.livestreamsales.model.network.rest.statuscode

enum class HttpStatusCodeType {
    Informational,
    Success,
    Redirection,
    ClientError,
    ServerError,
    Unknown;

    companion object{
        fun fromInt(statusCode: Int): HttpStatusCodeType{
            return when(statusCode){
                in 100..199 -> Informational
                in 200..299 -> Success
                in 300..399 -> Redirection
                in 400..499 -> ClientError
                in 500..599 -> ServerError
                else -> Unknown
            }
        }
    }
}