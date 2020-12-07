package com.example.livestreamsales.application

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

class LiveStreamSalesApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initializeFresco(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    private fun initializeFresco(context: Context){
        Fresco.initialize(context)
    }
}