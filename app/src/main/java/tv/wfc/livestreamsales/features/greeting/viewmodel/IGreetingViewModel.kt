package tv.wfc.livestreamsales.features.greeting.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage

interface IGreetingViewModel{
    val greetingPages: LiveData<Set<GreetingPage>>
    val nextDestinationEvent: LiveData<Destination>

    fun notifyGreetingIsShown()

    enum class Destination{
        LOG_IN,
        MAIN_APP_CONTENT
    }
}