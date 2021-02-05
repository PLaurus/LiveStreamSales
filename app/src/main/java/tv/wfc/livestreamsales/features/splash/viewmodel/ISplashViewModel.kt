package tv.wfc.livestreamsales.features.splash.viewmodel

import androidx.lifecycle.LiveData

interface ISplashViewModel {
    val nextDestination: LiveData<Destination>

    enum class Destination{
        GREETING,
        LOG_IN,
        MAIN_APP_CONTENT
    }
}