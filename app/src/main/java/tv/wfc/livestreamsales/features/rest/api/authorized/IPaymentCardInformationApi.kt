package tv.wfc.livestreamsales.features.rest.api.authorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST
import tv.wfc.livestreamsales.features.rest.api.base.IAuthorizedApi
import tv.wfc.livestreamsales.features.rest.model.api.request.LinkPaymentCardRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.response.LinkPaymentCardResponseBody

interface IPaymentCardInformationApi: IAuthorizedApi {
    @POST("credit-card")
    fun linkPaymentCard(
        @Body linkPaymentCardRequestBody: LinkPaymentCardRequestBody
    ): Single<LinkPaymentCardResponseBody>
}