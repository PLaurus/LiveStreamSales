package tv.wfc.livestreamsales.application.repository.paymentcardinformation

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.datastore.qualifiers.PaymentCardInformationRemoteDataStore
import tv.wfc.livestreamsales.application.model.paymentcardinformation.PaymentCardInformation
import tv.wfc.livestreamsales.application.model.paymentcardinformation.ResultOfStartingPaymentCardBinding
import tv.wfc.livestreamsales.application.storage.paymentcardinformation.IPaymentCardInformationDataStore
import javax.inject.Inject

class PaymentCardInformationRepository @Inject constructor(
    @PaymentCardInformationRemoteDataStore
    private val paymentCardInformationRemoteDataStore: IPaymentCardInformationDataStore,
    @IoScheduler
    private val ioScheduler: Scheduler
): IPaymentCardInformationRepository {
    override fun startPaymentCardBinding(token: String): Single<ResultOfStartingPaymentCardBinding> {
        return paymentCardInformationRemoteDataStore
            .startPaymentCardBinding(token)
            .subscribeOn(ioScheduler)
    }

    override fun getPaymentCardInformation(): Single<PaymentCardInformation> {
        return paymentCardInformationRemoteDataStore
            .getPaymentCardInformation()
            .subscribeOn(ioScheduler)
    }
}