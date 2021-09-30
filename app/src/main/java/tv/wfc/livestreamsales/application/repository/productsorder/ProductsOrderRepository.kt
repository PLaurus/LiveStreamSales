package tv.wfc.livestreamsales.application.repository.productsorder

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.datastore.qualifiers.ProductsOrderLocalDataStore
import tv.wfc.livestreamsales.application.di.modules.datastore.qualifiers.ProductsOrderRemoteDataStore
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.Product
import tv.wfc.livestreamsales.application.model.storage.StorageDataUpdateResult
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderDataStore
import javax.inject.Inject

class ProductsOrderRepository @Inject constructor(
    @ProductsOrderRemoteDataStore
    private val productsOrderRemoteDataStore: IProductsOrderDataStore,
    @ProductsOrderLocalDataStore
    private val productsOrderLocalDataStore: IProductsOrderDataStore,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsOrderRepository {
    override fun addProductToCart(product: Product, amount: Int): Completable {
        return productsOrderLocalDataStore
            .addProductToCart(product, amount)
            .subscribeOn(ioScheduler)
    }

    override fun addProductsToCart(products: List<Product>): Completable {
        return productsOrderLocalDataStore
            .addProductsToCart(products)
            .subscribeOn(ioScheduler)
    }

    override fun getOrderedProductsFromCartSingle(): Single<List<OrderedProduct>> {
        return productsOrderLocalDataStore
            .getOrderedProductsFromCartSingle()
            .subscribeOn(ioScheduler)
    }

    override fun getOrderedProductsFromCart(): Observable<List<OrderedProduct>> {
        return productsOrderLocalDataStore
            .getOrderedProductsFromCart()
            .subscribeOn(ioScheduler)
    }

    override fun removeProductFromCart(product: Product, amount: Int): Completable {
        return productsOrderLocalDataStore
            .removeProductFromCart(product, amount)
            .subscribeOn(ioScheduler)
    }

    override fun removeProductFromCart(productId: Long, amount: Int): Completable {
        return productsOrderLocalDataStore
            .removeProductFromCart(productId, amount)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductUnitsFromCart(product: Product): Completable {
        return productsOrderLocalDataStore
            .removeAllProductUnitsFromCart(product)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductUnitsFromCart(productId: Long): Completable {
        return productsOrderLocalDataStore
            .removeAllProductUnitsFromCart(productId)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(products: List<Product>): Completable {
        return productsOrderLocalDataStore
            .removeAllProductsUnitsFromCart()
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(vararg productIds: Long): Completable {
        return productsOrderLocalDataStore
            .removeAllProductsUnitsFromCart(*productIds)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(): Completable {
        return productsOrderLocalDataStore
            .removeAllProductsUnitsFromCart()
            .subscribeOn(ioScheduler)
    }

    override fun orderProducts(products: List<OrderedProduct>): Completable {
        return productsOrderRemoteDataStore
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
        return productsOrderRemoteDataStore
            .getOrders()
            .subscribeOn(ioScheduler)
    }

    override fun getOrder(id: Long): Single<Order> {
        return productsOrderRemoteDataStore
            .getOrder(id)
            .subscribeOn(ioScheduler)
    }

    override fun confirmOrder(
        orderId: Long,
        deliveryAddress: Address,
        deliveryDate: DateTime?
    ): Single<StorageDataUpdateResult> {
        return productsOrderRemoteDataStore
            .confirmOrder(orderId, deliveryAddress, deliveryDate)
            .subscribeOn(ioScheduler)
    }
}