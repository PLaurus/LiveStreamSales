package tv.wfc.livestreamsales.features.mainpage.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation
import tv.wfc.livestreamsales.application.viewmodels.base.IToBePreparedViewModel

interface IMainPageViewModel: IToBePreparedViewModel{
    val isDataBeingRefreshed: LiveData<Boolean>
    val liveBroadcasts: LiveData<List<BroadcastBaseInformation>>
    val announcements: LiveData<List<BroadcastBaseInformation>>

    fun refreshData()
    fun getLiveBroadcastTitleByPosition(position: Int): String
}