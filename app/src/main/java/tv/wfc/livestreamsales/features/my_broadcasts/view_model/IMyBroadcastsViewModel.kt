package tv.wfc.livestreamsales.features.my_broadcasts.view_model

import androidx.lifecycle.LiveData
import com.laurus.p.tools.livedata.LiveEvent
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.my_broadcasts.model.NextDestination

interface IMyBroadcastsViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>
    val nextDestinationEvent: LiveEvent<NextDestination>

    fun intentToCloseCurrentDestination()
}