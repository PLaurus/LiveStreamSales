package tv.wfc.livestreamsales.features.streamediting.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.streamediting.model.NextDestination

interface IStreamEditingViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>

    val nextDestinationEvent: LiveData<NextDestination>

    fun prepare(streamId: Long)

    fun requestToCloseCurrentDestination()
}