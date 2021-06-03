package tv.wfc.livestreamsales.application.storage.paymentcardinformation.remote

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.exception.storage.NoSuchDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.model.paymentcardinformation.PaymentCardInformation
import tv.wfc.livestreamsales.application.model.paymentcardinformation.ResultOfStartingPaymentCardBinding
import tv.wfc.livestreamsales.application.storage.paymentcardinformation.IPaymentCardInformationStorage
import tv.wfc.livestreamsales.features.rest.api.authorized.IPaymentCardInformationApi
import tv.wfc.livestreamsales.features.rest.model.api.request.BindPaymentCardRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.response.BindPaymentCardResponseBody
import javax.inject.Inject

private typealias RemotePaymentCardInformation = tv.wfc.livestreamsales.features.rest.model.paymentcardinformation.PaymentCardInformation

class PaymentCardInformationRemoteStorage @Inject constructor(
    private val paymentCardInformationApi: IPaymentCardInformationApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IPaymentCardInformationStorage {
    override fun startPaymentCardBinding(paymentToken: String): Single<ResultOfStartingPaymentCardBinding> {
        val linkPaymentCardRequestBody = BindPaymentCardRequestBody(paymentToken)
        return paymentCardInformationApi
            .bindPaymentCard(linkPaymentCardRequestBody)
            .map{ it.toResultOfStartingPaymentCardBinding() ?: throw ReceivedDataWithWrongFormatException() }
            .subscribeOn(ioScheduler)
    }

    override fun getPaymentCardInformation(): Single<PaymentCardInformation> {
        return paymentCardInformationApi
            .getPaymentCardInformation()
            .map { it.data ?: throw NoSuchDataInStorageException() }
            .map { remotePaymentCardInformation ->
                remotePaymentCardInformation.toLocalPaymentCardInformation() ?: throw ReceivedDataWithWrongFormatException()
            }
            .subscribeOn(ioScheduler)
    }

    private fun RemotePaymentCardInformation.toLocalPaymentCardInformation(): PaymentCardInformation?{
        val isBoundToAccount = this.isBoundToAccount ?: return null
        val cardNumber = this.cardNumber

        if(cardNumber == null && isBoundToAccount) return null

        return PaymentCardInformation(
            isBoundToAccount,
            cardNumber
        )
    }

    private fun BindPaymentCardResponseBody.toResultOfStartingPaymentCardBinding(): ResultOfStartingPaymentCardBinding?{
        val isBindingFlowStarted = this.isBindingFlowStarted ?: return null
        val errorMessage = this.errorMessage
        val confirmationUrl = this.confirmationUrl

        if(isBindingFlowStarted && confirmationUrl == null) return null

        return ResultOfStartingPaymentCardBinding(
            isBindingFlowStarted,
            errorMessage,
            confirmationUrl
        )
    }
}