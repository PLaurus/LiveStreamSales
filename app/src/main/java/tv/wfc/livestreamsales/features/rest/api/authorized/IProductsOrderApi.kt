package tv.wfc.livestreamsales.features.rest.api.authorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST
import tv.wfc.livestreamsales.features.rest.api.base.IAuthorizedApi
import tv.wfc.livestreamsales.features.rest.model.api.request.OrderProductsRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.response.OrderProductsResponseBody

interface IProductsOrderApi: IAuthorizedApi {
    @POST("order")
    fun orderProducts(
        @Body orderProductsRequestBody: OrderProductsRequestBody
    ): Single<OrderProductsResponseBody>
}