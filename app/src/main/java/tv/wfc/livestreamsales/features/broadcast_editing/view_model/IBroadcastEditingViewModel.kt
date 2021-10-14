package tv.wfc.livestreamsales.features.broadcast_editing.view_model

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.broadcast_editing.model.NextDestination

interface IBroadcastEditingViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>

    val nextDestinationEvent: LiveData<NextDestination>

    fun requestToCloseCurrentDestination()
}