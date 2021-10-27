package tv.wfc.livestreamsales.features.mainpage.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.stream.PublicStream

interface IMainPageViewModel: INeedPreparationViewModel {
    val isDataBeingRefreshed: LiveData<Boolean>
    val liveStreams: LiveData<List<PublicStream>>
    val announcements: LiveData<List<PublicStream>>

    fun refreshData()
    fun getLiveBroadcastTitleByPosition(position: Int): String
}