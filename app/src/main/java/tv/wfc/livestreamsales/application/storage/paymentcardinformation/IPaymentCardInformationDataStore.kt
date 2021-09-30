package tv.wfc.livestreamsales.application.storage.paymentcardinformation

import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.paymentcardinformation.PaymentCardInformation
import tv.wfc.livestreamsales.application.model.paymentcardinformation.ResultOfStartingPaymentCardBinding

interface IPaymentCardInformationDataStore {
    fun startPaymentCardBinding(paymentToken: String): Single<ResultOfStartingPaymentCardBinding>
    fun getPaymentCardInformation(): Single<PaymentCardInformation>
}