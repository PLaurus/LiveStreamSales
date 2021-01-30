package com.example.livestreamsales.viewmodels.greeting

import androidx.lifecycle.LiveData
import com.example.livestreamsales.model.application.greetingpage.GreetingPage

interface IGreetingViewModel{
    val greetingPages: LiveData<Set<GreetingPage>>
    val nextDestinationEvent: LiveData<Destination>

    fun notifyGreetingIsShown()

    enum class Destination{
        AUTHORIZATION,
        MAIN
    }
}