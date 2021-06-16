package tv.wfc.livestreamsales.application.repository.productsorder

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct

interface IProductsOrderRepository {
    fun orderProducts(products: List<OrderedProduct>): Completable
    fun getOrders(): Single<List<Order>>
    fun getOrder(id: Long): Single<Order>
    fun updateOrderDeliveryAddress(orderId: Long, deliveryAddress: Address): Completable
    fun updateOrderDeliveryDate(orderId: Long, deliveryDate: DateTime): Completable
}