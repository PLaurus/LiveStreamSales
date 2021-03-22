package tv.wfc.contentloader.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.model.ViewModelPreparationState

interface INeedPreparationViewModel {
    val dataPreparationState: LiveData<ViewModelPreparationState>
}