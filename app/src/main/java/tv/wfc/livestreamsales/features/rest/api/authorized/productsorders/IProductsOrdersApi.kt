package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.confirmorder.ConfirmOrderRequestBody
import tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.confirmorder.ConfirmOrderResponseBody
import tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.getorders.GetOrderResponseBody
import tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.getorders.GetOrdersResponseBody
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.model.api.orderproducts.OrderProductsRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.orderproducts.OrderProductsResponseBody

interface IProductsOrdersApi: IApi {
    @POST("order")
    fun orderProducts(
        @Body orderProductsRequestBody: OrderProductsRequestBody
    ): Single<OrderProductsResponseBody>

    @GET("order")
    fun getOrders(): Single<GetOrdersResponseBody>

    @GET("order/{id}/edit")
    fun getOrder(
        @Path("id")
        orderId: Long
    ): Single<GetOrderResponseBody>

    @POST("order/{id}")
    fun confirmOrder(
        @Path("id")
        orderId: Long,
        @Body confirmOrderRequestBody: ConfirmOrderRequestBody
    ): Single<ConfirmOrderResponseBody>
}