package tv.wfc.livestreamsales.application.repository.paymentcardinformation

import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.paymentcardinformation.PaymentCardInformation
import tv.wfc.livestreamsales.application.model.paymentcardinformation.ResultOfStartingPaymentCardBinding

interface IPaymentCardInformationRepository {
    fun startPaymentCardBinding(token: String): Single<ResultOfStartingPaymentCardBinding>
    fun getPaymentCardInformation(): Single<PaymentCardInformation>
}