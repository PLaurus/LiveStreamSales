package tv.wfc.livestreamsales.viewmodels.broadcastsinformation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tv.wfc.livestreamsales.model.application.viewmodel.ViewModelPreparationState
import javax.inject.Inject

class BroadcastsInformationViewModel @Inject constructor(

): ViewModel(), IBroadcastsInformationViewModel {
    override val dataPreparationState: LiveData<ViewModelPreparationState>
        get() = TODO("Not yet implemented")
}