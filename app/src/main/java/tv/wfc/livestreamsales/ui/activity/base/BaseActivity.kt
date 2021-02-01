package tv.wfc.livestreamsales.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import tv.wfc.livestreamsales.application.LiveStreamSalesApplication
import tv.wfc.livestreamsales.di.components.app.AppComponent

abstract class BaseActivity: AppCompatActivity() {
    protected val appComponent: AppComponent by lazy{
        (application as LiveStreamSalesApplication).appComponent
    }
}