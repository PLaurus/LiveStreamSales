package tv.wfc.livestreamsales.features.paymentcardinformation.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel

interface IPaymentCardInformationViewModel: INeedPreparationViewModel{
    val isAnyOperationInProgress: LiveData<Boolean>
    val paymentCardBindingParameters: LiveData<PaymentParameters>
    val paymentCardBindingError: LiveData<String?>
    val paymentCardBindingConfirmationUrl: LiveData<String?>
    val isPaymentCardBound: LiveData<Boolean>
    val paymentCardBindingState: LiveData<CardBindingState>
    val boundPaymentCardNumber: LiveData<String?>

    fun startPaymentCardBinding(tokenizationResultIntent: Intent)
    fun waitUntilCardIsBound()
    fun notify3dsErrorOccurred(`3dsResultIntent`: Intent)

    enum class CardBindingState{
        NotBound,
        BindingFlowStarted,
        WillBeBoundSoon,
        Bound
    }
}