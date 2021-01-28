package com.example.livestreamsales.viewmodels.splash

import androidx.lifecycle.LiveData

interface ISplashViewModel {
    val nextDestination: LiveData<Destination>

    enum class Destination{
        GREETING,
        AUTHORIZATION,
        MAIN
    }
}