package tv.wfc.livestreamsales.application.repository.productsorder

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.ProductsOrderRemoteStorage
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderStorage
import javax.inject.Inject

class ProductsOrderRepository @Inject constructor(
    @ProductsOrderRemoteStorage
    private val productsOrderRemoteStorage: IProductsOrderStorage,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsOrderRepository {
    override fun orderProducts(products: List<OrderedProduct>): Completable {
        return productsOrderRemoteStorage
            .orderProducts(products)
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

    override fun updateOrderDeliveryAddress(orderId: Long, deliveryAddress: Address): Completable {
        return productsOrderRemoteStorage
            .updateOrderDeliveryAddress(orderId, deliveryAddress)
            .subscribeOn(ioScheduler)
    }

    override fun updateOrderDeliveryDate(orderId: Long, deliveryDate: DateTime): Completable {
        return productsOrderRemoteStorage
            .updateOrderDeliveryDate(orderId, deliveryDate)
            .subscribeOn(ioScheduler)
    }
}