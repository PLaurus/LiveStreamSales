package tv.wfc.livestreamsales.features.needpaymentinformation.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface INeedPaymentInformationViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>
}