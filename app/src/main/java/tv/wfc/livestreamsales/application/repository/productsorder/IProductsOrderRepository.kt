package tv.wfc.livestreamsales.application.repository.productsorder

import io.reactivex.rxjava3.core.Completable
import tv.wfc.livestreamsales.application.model.products.order.ProductInCart

interface IProductsOrderRepository {
    fun orderProducts(products: List<ProductInCart>): Completable
}