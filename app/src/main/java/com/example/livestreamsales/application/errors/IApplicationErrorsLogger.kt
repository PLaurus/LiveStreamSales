package com.example.livestreamsales.application.errors

interface IApplicationErrorsLogger {
    fun logError(throwable: Throwable)
}