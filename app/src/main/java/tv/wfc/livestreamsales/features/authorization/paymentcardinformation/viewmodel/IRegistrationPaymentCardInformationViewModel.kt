package tv.wfc.livestreamsales.features.authorization.paymentcardinformation.viewmodel

import androidx.lifecycle.LiveData
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface IRegistrationPaymentCardInformationViewModel: INeedPreparationViewModel{
    val isAnyOperationInProgress: LiveData<Boolean>

    fun updatePaymentCardInformation(token: String)
}