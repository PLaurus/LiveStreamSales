package tv.wfc.livestreamsales.features.mainpage.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation
import tv.wfc.livestreamsales.application.viewmodels.base.IToBePreparedViewModel

interface IMainPageViewModel: IToBePreparedViewModel{
    val isDataBeingRefreshed: LiveData<Boolean>
    val liveBroadcasts: LiveData<List<BroadcastInformation>>
    val announcements: LiveData<List<BroadcastInformation>>

    fun refreshData()
    fun getLiveBroadcastTitleByPosition(position: Int): String
}