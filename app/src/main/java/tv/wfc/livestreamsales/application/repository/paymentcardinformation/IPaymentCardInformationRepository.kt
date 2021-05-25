package tv.wfc.livestreamsales.application.repository.paymentcardinformation

import io.reactivex.rxjava3.core.Completable

interface IPaymentCardInformationRepository {
    fun updatePaymentCardInformation(token: String): Completable
}