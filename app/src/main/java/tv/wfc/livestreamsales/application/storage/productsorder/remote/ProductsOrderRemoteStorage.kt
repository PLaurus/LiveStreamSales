package tv.wfc.livestreamsales.application.storage.productsorder.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.exception.storage.FailedToUpdateDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.model.products.order.ProductInCart
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderStorage
import tv.wfc.livestreamsales.features.rest.api.authorized.IProductsOrderApi
import tv.wfc.livestreamsales.features.rest.model.api.request.OrderProductsRequestBody
import tv.wfc.livestreamsales.features.rest.model.productsorder.Product
import javax.inject.Inject

class ProductsOrderRemoteStorage @Inject constructor(
    private val productsOrderApi: IProductsOrderApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsOrderStorage {
    override fun orderProducts(products: List<ProductInCart>): Completable {
        val orderProductsRequestBody = OrderProductsRequestBody(products.toRemoteProductsOrder())
        return productsOrderApi
            .orderProducts(orderProductsRequestBody)
            .flatMapCompletable {
                when(it.areProductsOrdered){
                    true -> Completable.complete()
                    false -> Completable.error(FailedToUpdateDataInStorageException())
                    else -> Completable.error(ReceivedDataWithWrongFormatException())
                }
            }
            .subscribeOn(ioScheduler)
    }

    private fun List<ProductInCart>.toRemoteProductsOrder(): List<Product>{
        return map { Product(it.product.id, it.amount) }
    }
}