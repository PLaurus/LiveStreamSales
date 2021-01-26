package com.example.livestreamsales.application.errors

import android.util.Log
import javax.inject.Inject

class ApplicationErrorsLogger @Inject constructor(

): IApplicationErrorsLogger {
    private val logTag = this::class.simpleName

    override fun logError(throwable: Throwable){
        Log.e(logTag, throwable.message, throwable)
    }
}