package tv.wfc.livestreamsales.features.paymentcardinformation.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface IPaymentCardInformationViewModel: INeedPreparationViewModel{
    val isDataBeingRefreshed: LiveData<Boolean>
    val isAnyOperationInProgress: LiveData<Boolean>
    val paymentCardBindingParameters: LiveData<PaymentParameters>
    val paymentCardBindingError: LiveData<String?>
    val paymentCardBindingConfirmationUrl: LiveData<String?>
    val paymentCardBindingState: LiveData<CardBindingState>

    fun refreshData()
    fun startPaymentCardBinding(tokenizationResultIntent: Intent)
    fun notify3dsErrorOccurred(`3dsResultIntent`: Intent)

    sealed class CardBindingState{
        data class Bound(val cardNumber: String): CardBindingState()
        object WillBeBoundSoon: CardBindingState()
        object NotBound: CardBindingState()
    }
}