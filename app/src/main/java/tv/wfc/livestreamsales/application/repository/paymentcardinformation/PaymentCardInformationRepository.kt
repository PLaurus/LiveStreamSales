package tv.wfc.livestreamsales.application.repository.paymentcardinformation

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.PaymentCardInformationRemoteStorage
import tv.wfc.livestreamsales.application.model.paymentcardinformation.PaymentCardInformation
import tv.wfc.livestreamsales.application.model.paymentcardinformation.ResultOfStartingPaymentCardBinding
import tv.wfc.livestreamsales.application.storage.paymentcardinformation.IPaymentCardInformationStorage
import javax.inject.Inject

class PaymentCardInformationRepository @Inject constructor(
    @PaymentCardInformationRemoteStorage
    private val paymentCardInformationRemoteStorage: IPaymentCardInformationStorage,
    @IoScheduler
    private val ioScheduler: Scheduler
): IPaymentCardInformationRepository {
    override fun startPaymentCardBinding(token: String): Single<ResultOfStartingPaymentCardBinding> {
        return paymentCardInformationRemoteStorage
            .startPaymentCardBinding(token)
            .subscribeOn(ioScheduler)
    }

    override fun getPaymentCardInformation(): Single<PaymentCardInformation> {
        return paymentCardInformationRemoteStorage
            .getPaymentCardInformation()
            .subscribeOn(ioScheduler)
    }
}