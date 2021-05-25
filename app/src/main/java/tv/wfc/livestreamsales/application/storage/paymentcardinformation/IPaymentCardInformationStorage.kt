package tv.wfc.livestreamsales.application.storage.paymentcardinformation

import io.reactivex.rxjava3.core.Completable

interface IPaymentCardInformationStorage {
    fun updatePaymentCardInformation(token: String): Completable
}