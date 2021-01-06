package com.example.livestreamsales.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.application.LiveStreamSalesApplication
import com.example.livestreamsales.di.components.app.AppComponent

open class BaseActivity: AppCompatActivity() {
    protected val appComponent: AppComponent = (application as LiveStreamSalesApplication).appComponent
}