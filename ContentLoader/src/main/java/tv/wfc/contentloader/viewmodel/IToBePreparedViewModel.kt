package tv.wfc.contentloader.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.model.ViewModelPreparationState

interface IToBePreparedViewModel {
    val dataPreparationState: LiveData<ViewModelPreparationState>
}