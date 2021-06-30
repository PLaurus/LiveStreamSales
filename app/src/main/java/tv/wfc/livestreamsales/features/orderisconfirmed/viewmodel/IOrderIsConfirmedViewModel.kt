package tv.wfc.livestreamsales.features.orderisconfirmed.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface IOrderIsConfirmedViewModel: INeedPreparationViewModel{
    val isAnyOperationInProgress: LiveData<Boolean>
}