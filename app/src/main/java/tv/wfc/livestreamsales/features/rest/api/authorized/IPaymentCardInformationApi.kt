package tv.wfc.livestreamsales.features.rest.api.authorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.model.api.bindpaymentcard.BindPaymentCardRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.bindpaymentcard.BindPaymentCardResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.getpaymentcardinformation.GetPaymentCardInformationResponseBody

interface IPaymentCardInformationApi: IApi {
    @POST("credit-card")
    fun bindPaymentCard(
        @Body bindPaymentCardRequestBody: BindPaymentCardRequestBody
    ): Single<BindPaymentCardResponseBody>

    @GET("credit-card")
    fun getPaymentCardInformation(): Single<GetPaymentCardInformationResponseBody>
}