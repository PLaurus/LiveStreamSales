package tv.wfc.livestreamsales.application.viewmodels.base

import androidx.lifecycle.LiveData
import tv.wfc.livestreamsales.application.model.viewmodel.ViewModelPreparationState

interface IToBePreparedViewModel {
    val dataPreparationState: LiveData<ViewModelPreparationState>
}