package tv.wfc.livestreamsales.features.mystreams.viewmodel

import androidx.lifecycle.LiveData
import com.laurus.p.tools.livedata.LiveEvent
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.features.mystreams.model.NextDestination

interface IMyStreamsViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>

    val isDataBeingRefreshed: LiveData<Boolean>

    val nextDestinationEvent: LiveEvent<NextDestination>

    val myStreams: LiveData<List<MyStream>>

    fun refreshData()

    fun intentToCloseCurrentDestination()
}