package tv.wfc.livestreamsales.application.storage.paymentcardinformation.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.exception.storage.FailedToUpdateDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.storage.paymentcardinformation.IPaymentCardInformationStorage
import tv.wfc.livestreamsales.features.rest.api.authorized.IPaymentCardInformationApi
import tv.wfc.livestreamsales.features.rest.model.api.request.LinkPaymentCardRequestBody
import javax.inject.Inject

class PaymentCardInformationRemoteStorage @Inject constructor(
    private val paymentCardInformationApi: IPaymentCardInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IPaymentCardInformationStorage {
    override fun updatePaymentCardInformation(token: String): Completable {
        val linkPaymentCardRequestBody = LinkPaymentCardRequestBody(token)
        return paymentCardInformationApi
            .linkPaymentCard(linkPaymentCardRequestBody)
            .map{ it.isPaymentCardLinked ?: throw ReceivedDataWithWrongFormatException() }
            .flatMapCompletable { isPaymentCardLinked ->
                if(isPaymentCardLinked){
                    Completable.complete()
                } else{
                    Completable.error(FailedToUpdateDataInStorageException())
                }
            }
            .subscribeOn(ioScheduler)
    }
}