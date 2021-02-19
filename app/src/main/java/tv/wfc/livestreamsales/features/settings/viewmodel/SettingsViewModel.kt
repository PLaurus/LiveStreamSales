package tv.wfc.livestreamsales.features.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tv.wfc.contentloader.model.ViewModelPreparationState
import javax.inject.Inject

class SettingsViewModel @Inject constructor(

): ViewModel(), ISettingsViewModel {
    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsPrepared)
}