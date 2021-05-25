package tv.wfc.livestreamsales.application.repository.paymentcardinformation

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.PaymentCardInformationRemoteStorage
import tv.wfc.livestreamsales.application.storage.paymentcardinformation.IPaymentCardInformationStorage
import javax.inject.Inject

class PaymentCardInformationRepository @Inject constructor(
    @PaymentCardInformationRemoteStorage
    private val paymentCardInformationRemoteStorage: IPaymentCardInformationStorage,
    @IoScheduler
    private val ioScheduler: Scheduler
): IPaymentCardInformationRepository {
    override fun updatePaymentCardInformation(token: String): Completable {
        return paymentCardInformationRemoteStorage
            .updatePaymentCardInformation(token)
            .subscribeOn(ioScheduler)
    }
}