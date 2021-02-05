package tv.wfc.livestreamsales.features.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.application.model.viewmodel.ViewModelPreparationState
import javax.inject.Inject

class HomeViewModel @Inject constructor(

): ViewModel(), IHomeViewModel {
    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)
}