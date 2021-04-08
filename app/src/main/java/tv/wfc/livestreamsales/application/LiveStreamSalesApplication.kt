package tv.wfc.livestreamsales.application

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import tv.wfc.livestreamsales.application.di.AppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import tv.wfc.livestreamsales.application.di.DaggerAppComponent

class LiveStreamSalesApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        initializeFresco(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initializeFresco(context: Context){
        Fresco.initialize(context)
    }
}