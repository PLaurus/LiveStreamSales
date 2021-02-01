package tv.wfc.livestreamsales.viewmodels.base

import androidx.lifecycle.LiveData
import tv.wfc.livestreamsales.model.application.viewmodel.ViewModelPreparationState

interface IToBePreparedViewModel {
    val dataPreparationState: LiveData<ViewModelPreparationState>
}