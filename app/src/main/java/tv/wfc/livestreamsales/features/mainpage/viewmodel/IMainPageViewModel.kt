package tv.wfc.livestreamsales.features.mainpage.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast

interface IMainPageViewModel: INeedPreparationViewModel {
    val isDataBeingRefreshed: LiveData<Boolean>
    val liveBroadcasts: LiveData<List<Broadcast>>
    val announcements: LiveData<List<Broadcast>>

    fun refreshData()
    fun getLiveBroadcastTitleByPosition(position: Int): String
}