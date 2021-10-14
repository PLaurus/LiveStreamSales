package tv.wfc.livestreamsales.features.broadcast_creation.view_model

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.broadcast_creation.model.NextDestination

interface IBroadcastCreationViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>

    val nextDestinationEvent: LiveData<NextDestination>

    fun intentToCloseCurrentDestination()
}