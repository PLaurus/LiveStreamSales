package tv.wfc.livestreamsales.application.repository.productsorder

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsOrderLocalStorage
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsOrderRemoteStorage
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.Product
import tv.wfc.livestreamsales.application.model.storage.StorageDataUpdateResult
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderStorage
import javax.inject.Inject

class ProductsOrderRepository @Inject constructor(
    @ProductsOrderRemoteStorage
    private val productsOrderRemoteStorage: IProductsOrderStorage,
    @ProductsOrderLocalStorage
    private val productsOrderLocalStorage: IProductsOrderStorage,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsOrderRepository {
    override fun addProductToCart(product: Product, amount: Int): Completable {
        return productsOrderLocalStorage
            .addProductToCart(product, amount)
            .subscribeOn(ioScheduler)
    }

    override fun addProductsToCart(products: List<Product>): Completable {
        return productsOrderLocalStorage
            .addProductsToCart(products)
            .subscribeOn(ioScheduler)
    }

    override fun getOrderedProductsFromCartSingle(): Single<List<OrderedProduct>> {
        return productsOrderLocalStorage
            .getOrderedProductsFromCartSingle()
            .subscribeOn(ioScheduler)
    }

    override fun getOrderedProductsFromCart(): Observable<List<OrderedProduct>> {
        return productsOrderLocalStorage
            .getOrderedProductsFromCart()
            .subscribeOn(ioScheduler)
    }

    override fun removeProductFromCart(product: Product, amount: Int): Completable {
        return productsOrderLocalStorage
            .removeProductFromCart(product, amount)
            .subscribeOn(ioScheduler)
    }

    override fun removeProductFromCart(productId: Long, amount: Int): Completable {
        return productsOrderLocalStorage
            .removeProductFromCart(productId, amount)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductUnitsFromCart(product: Product): Completable {
        return productsOrderLocalStorage
            .removeAllProductUnitsFromCart(product)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductUnitsFromCart(productId: Long): Completable {
        return productsOrderLocalStorage
            .removeAllProductUnitsFromCart(productId)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(products: List<Product>): Completable {
        return productsOrderLocalStorage
            .removeAllProductsUnitsFromCart()
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(vararg productIds: Long): Completable {
        return productsOrderLocalStorage
            .removeAllProductsUnitsFromCart(*productIds)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(): Completable {
        return productsOrderLocalStorage
            .removeAllProductsUnitsFromCart()
            .subscribeOn(ioScheduler)
    }

    override fun orderProducts(products: List<OrderedProduct>): Completable {
        return productsOrderRemoteStorage
            .orderProducts(products)
            .subscribeOn(ioScheduler)
    }

    override fun orderProductsFromCart(): Completable {
        return getOrderedProductsFromCartSingle()
            .flatMapCompletable (::orderProducts)
            .andThen(removeAllProductsUnitsFromCart())
            .subscribeOn(ioScheduler)
    }

    override fun getOrders(): Single<List<Order>> {
        return productsOrderRemoteStorage
            .getOrders()
            .subscribeOn(ioScheduler)
    }

    override fun getOrder(id: Long): Single<Order> {
        return productsOrderRemoteStorage
            .getOrder(id)
            .subscribeOn(ioScheduler)
    }

    override fun confirmOrder(
        orderId: Long,
        deliveryAddress: Address,
        deliveryDate: DateTime?
    ): Single<StorageDataUpdateResult> {
        return productsOrderRemoteStorage
            .confirmOrder(orderId, deliveryAddress, deliveryDate)
            .subscribeOn(ioScheduler)
    }
}