package tv.wfc.livestreamsales.features.rest.api.authorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tv.wfc.livestreamsales.features.rest.api.base.IAuthorizedApi
import tv.wfc.livestreamsales.features.rest.model.api.request.BindPaymentCardRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.response.BindPaymentCardResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.response.GetPaymentCardInformationResponseBody

interface IPaymentCardInformationApi: IAuthorizedApi {
    @POST("credit-card")
    fun bindPaymentCard(
        @Body bindPaymentCardRequestBody: BindPaymentCardRequestBody
    ): Single<BindPaymentCardResponseBody>

    @GET("credit-card")
    fun getPaymentCardInformation(): Single<GetPaymentCardInformationResponseBody>
}