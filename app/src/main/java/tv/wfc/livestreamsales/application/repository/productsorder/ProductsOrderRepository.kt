package tv.wfc.livestreamsales.application.repository.productsorder

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsOrderRemoteStorage
import tv.wfc.livestreamsales.application.model.products.order.ProductInCart
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderStorage
import javax.inject.Inject

class ProductsOrderRepository @Inject constructor(
    @ProductsOrderRemoteStorage
    private val productsOrderRemoteStorage: IProductsOrderStorage,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsOrderRepository {
    override fun orderProducts(products: List<ProductInCart>): Completable {
        return productsOrderRemoteStorage
            .orderProducts(products)
            .subscribeOn(ioScheduler)
    }
}