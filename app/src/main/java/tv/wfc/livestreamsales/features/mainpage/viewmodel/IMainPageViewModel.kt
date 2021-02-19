package tv.wfc.livestreamsales.features.mainpage.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.IToBePreparedViewModel
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation

interface IMainPageViewModel: IToBePreparedViewModel {
    val isDataBeingRefreshed: LiveData<Boolean>
    val liveBroadcasts: LiveData<List<BroadcastInformation>>
    val announcements: LiveData<List<BroadcastInformation>>

    fun refreshData()
    fun getLiveBroadcastTitleByPosition(position: Int): String
}