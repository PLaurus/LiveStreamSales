package tv.wfc.livestreamsales.features.rest.api.notauthorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.model.api.getproducts.GetProductsResponseBody

interface IProductsApi: IApi {
    @GET("products")
    fun getProducts(
        @Query("broadcast_id") broadcastId: Long
    ): Single<GetProductsResponseBody>
}