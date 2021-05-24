package tv.wfc.livestreamsales.features.greeting.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage

interface IGreetingViewModel: INeedPreparationViewModel{
    val greetingPages: LiveData<Set<GreetingPage>>
    val isAnyOperationInProgress: LiveData<Boolean>
    val isShownStateSaved: LiveData<Boolean>

    fun saveGreetingIsShown()
}