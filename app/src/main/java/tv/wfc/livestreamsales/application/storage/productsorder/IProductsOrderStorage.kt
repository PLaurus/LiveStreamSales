package tv.wfc.livestreamsales.application.storage.productsorder

import io.reactivex.rxjava3.core.Completable
import tv.wfc.livestreamsales.application.model.products.order.ProductInCart

interface IProductsOrderStorage {
    fun orderProducts(products: List<ProductInCart>): Completable
}