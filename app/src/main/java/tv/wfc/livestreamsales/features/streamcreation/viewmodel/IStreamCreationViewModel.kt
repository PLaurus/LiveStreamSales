package tv.wfc.livestreamsales.features.streamcreation.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.streamcreation.model.NextDestination

interface IStreamCreationViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>

    val nextDestinationEvent: LiveData<NextDestination>

    fun intentToCloseCurrentDestination()
}