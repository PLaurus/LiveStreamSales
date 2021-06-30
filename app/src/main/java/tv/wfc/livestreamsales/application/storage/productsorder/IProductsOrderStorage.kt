package tv.wfc.livestreamsales.application.storage.productsorder

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.storage.StorageDataUpdateResult

interface IProductsOrderStorage {
    fun orderProducts(products: List<OrderedProduct>): Completable
    fun getOrders(): Single<List<Order>>
    fun getOrder(id: Long): Single<Order>
    fun confirmOrder(orderId: Long, deliveryAddress: Address, deliveryDate: DateTime): Single<StorageDataUpdateResult>
}