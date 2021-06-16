package tv.wfc.livestreamsales.features.rest.api.authorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tv.wfc.livestreamsales.features.rest.api.base.IAuthorizedApi
import tv.wfc.livestreamsales.features.rest.model.api.editorderaddress.EditOrderAddressRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.editorderaddress.EditOrderAddressResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.editorderdeliverydate.EditOrderDeliveryDateRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.editorderdeliverydate.EditOrderDeliveryDateResponse
import tv.wfc.livestreamsales.features.rest.model.api.getorders.GetOrderResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.orderproducts.OrderProductsRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.getorders.GetOrdersResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.orderproducts.OrderProductsResponseBody

interface IProductsOrdersApi: IAuthorizedApi {
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
    fun editOrderAddress(
        @Path("id")
        orderId: Long,
        @Body editOrderAddressRequestBody: EditOrderAddressRequestBody
    ): Single<EditOrderAddressResponseBody>

    @POST("order/{id}")
    fun editOrderDeliveryDate(
        @Path("id")
        orderId: Long,
        @Body editOrderDeliveryDateRequestBody: EditOrderDeliveryDateRequestBody
    ): Single<EditOrderDeliveryDateResponse>
}