package com.example.livestreamsales.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import com.example.livestreamsales.application.LiveStreamSalesApplication
import com.example.livestreamsales.di.components.app.AppComponent

abstract class BaseActivity: AppCompatActivity() {
    protected val appComponent: AppComponent by lazy{
        (application as LiveStreamSalesApplication).appComponent
    }
}