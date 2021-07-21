package tv.wfc.livestreamsales.application.storage.productsorder.local

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.database.tables.CartDao
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.Product
import tv.wfc.livestreamsales.application.model.storage.StorageDataUpdateResult
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderStorage
import javax.inject.Inject

class ProductsOrderLocalStorage @Inject constructor(
    private val cartDao: CartDao,
    @IoScheduler
    private val ioScheduler: Scheduler,
    @ComputationScheduler
    private val computationScheduler: Scheduler
): IProductsOrderStorage{
    override fun addProductToCart(product: Product, amount: Int): Completable {
        return cartDao
            .rxAdd(product, amount)
            .subscribeOn(ioScheduler)
    }

    override fun addProductsToCart(products: List<Product>): Completable {
        return Single
            .just(products)
            .map { it.map(cartDao::rxAdd) }
            .subscribeOn(computationScheduler)
            .flatMapCompletable(Completable::merge)
            .subscribeOn(ioScheduler)
    }

    override fun getOrderedProductsFromCartSingle(): Single<List<OrderedProduct>> {
        return cartDao
            .rxGetAllSingle()
            .subscribeOn(ioScheduler)
    }

    override fun getOrderedProductsFromCart(): Observable<List<OrderedProduct>> {
        return cartDao
            .rxGetAll()
            .subscribeOn(ioScheduler)
    }

    override fun removeProductFromCart(product: Product, amount: Int): Completable {
        return cartDao
            .rxRemove(product, amount)
            .subscribeOn(ioScheduler)
    }

    override fun removeProductFromCart(productId: Long, amount: Int): Completable {
        return cartDao
            .rxRemove(productId, amount)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductUnitsFromCart(product: Product): Completable {
        return cartDao
            .rxRemove(product)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductUnitsFromCart(productId: Long): Completable {
        return cartDao
            .rxRemove(productId)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(products: List<Product>): Completable {
        return Single
            .just(products)
            .map { it.map(cartDao::rxRemove) }
            .subscribeOn(computationScheduler)
            .flatMapCompletable(Completable::merge)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(vararg productIds: Long): Completable {
        return Single
            .just(productIds)
            .map { it.map(cartDao::rxRemove) }
            .subscribeOn(computationScheduler)
            .flatMapCompletable(Completable::merge)
            .subscribeOn(ioScheduler)
    }

    override fun removeAllProductsUnitsFromCart(): Completable {
        return cartDao
            .rxDeleteAll()
            .subscribeOn(ioScheduler)
    }

    override fun orderProducts(products: List<OrderedProduct>): Completable {
        return Completable
            .error(NotImplementedError())
            .subscribeOn(ioScheduler)
    }

    override fun getOrders(): Single<List<Order>> {
        return Single
            .error<List<Order>>(NotImplementedError())
            .subscribeOn(ioScheduler)
    }

    override fun getOrder(id: Long): Single<Order> {
        return Single
            .error<Order>(NotImplementedError())
            .subscribeOn(ioScheduler)
    }

    override fun confirmOrder(
        orderId: Long,
        deliveryAddress: Address,
        deliveryDate: DateTime
    ): Single<StorageDataUpdateResult> {
        return Single
            .error<StorageDataUpdateResult>(NotImplementedError())
            .subscribeOn(ioScheduler)
    }
}