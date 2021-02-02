package tv.wfc.livestreamsales.viewmodels.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import javax.inject.Inject

class HomeViewModel @Inject constructor(

): ViewModel(), IHomeViewModel{
    override val dataPreparationState: LiveData<ViewModelPreparationState>
        get() = TODO("Not yet implemented")
}