package tv.wfc.livestreamsales.features.home.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface IHomeViewModel: INeedPreparationViewModel{
    val isUserAuthorized: LiveData<Boolean>
    val isCardLinkedToAccount: LiveData<Boolean>
}