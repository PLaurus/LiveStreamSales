package tv.wfc.livestreamsales.application

import android.app.Application
import android.content.Context
import tv.wfc.livestreamsales.di.components.app.AppComponent
import tv.wfc.livestreamsales.di.components.app.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco

class LiveStreamSalesApplication: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        initializeFresco(this)
    }

    private fun initializeFresco(context: Context){
        Fresco.initialize(context)
    }
}