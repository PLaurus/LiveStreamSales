package tv.wfc.livestreamsales.application.ui.base

import androidx.appcompat.app.AppCompatActivity
import tv.wfc.livestreamsales.application.LiveStreamSalesApplication
import tv.wfc.livestreamsales.application.di.AppComponent

abstract class BaseActivity: AppCompatActivity() {
    protected val appComponent: AppComponent by lazy{
        (application as LiveStreamSalesApplication).appComponent
    }
}