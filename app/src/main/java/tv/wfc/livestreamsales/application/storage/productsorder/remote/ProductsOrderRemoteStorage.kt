package tv.wfc.livestreamsales.application.storage.productsorder.remote

import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.exception.storage.FailedToUpdateDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.NoSuchDataInStorageException
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderRecipient
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.Product
import tv.wfc.livestreamsales.application.storage.productsorder.IProductsOrderStorage
import tv.wfc.livestreamsales.features.rest.api.authorized.IProductsOrdersApi
import tv.wfc.livestreamsales.features.rest.model.api.editorderaddress.EditOrderAddressRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.editorderdeliverydate.EditOrderDeliveryDateRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.orderproducts.OrderProductsRequestBody
import javax.inject.Inject

private typealias RemoteOrder = tv.wfc.livestreamsales.features.rest.model.api.getorders.Order
private typealias RemoteOrderedProduct = tv.wfc.livestreamsales.features.rest.model.api.getorders.OrderedProduct
private typealias RemoteProduct = tv.wfc.livestreamsales.features.rest.model.api.getorders.Product
private typealias RemoteAddress = tv.wfc.livestreamsales.features.rest.model.api.getorders.Address
private typealias RemoteOrderRecipient = tv.wfc.livestreamsales.features.rest.model.api.getorders.OrderRecipient

class ProductsOrderRemoteStorage @Inject constructor(
    private val productsOrdersApi: IProductsOrdersApi,
    @IoScheduler
    private val ioScheduler: Scheduler
): IProductsOrderStorage {
    override fun orderProducts(products: List<OrderedProduct>): Completable {
        val orderProductsRequestBody = OrderProductsRequestBody(products.toRemoteProductsOrder())
        return productsOrdersApi
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

    override fun getOrders(): Single<List<Order>> {
        return productsOrdersApi
            .getOrders()
            .map { it.orders ?: throw NoSuchDataInStorageException() }
            .map { orders -> orders.mapNotNull { it.toApplicationOrder() } }
            .subscribeOn(ioScheduler)
    }

    override fun getOrder(id: Long): Single<Order> {
        return productsOrdersApi
            .getOrder(id)
            .map{ it.order ?: throw NoSuchDataInStorageException() }
            .map{ remoteOrder ->
                remoteOrder.toApplicationOrder() ?: throw ReceivedDataWithWrongFormatException()
            }
            .subscribeOn(ioScheduler)

    }

    override fun updateOrderDeliveryAddress(orderId: Long, deliveryAddress: Address): Completable {
        val editOrderAddressRequestBody = deliveryAddress.toEditOrderAddressRequestBody()
        return productsOrdersApi
            .editOrderAddress(orderId, editOrderAddressRequestBody)
            .flatMapCompletable {
                when(it.isOrderUpdated){
                    true -> Completable.complete()
                    false -> Completable.error(FailedToUpdateDataInStorageException())
                    else -> Completable.error(ReceivedDataWithWrongFormatException())
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateOrderDeliveryDate(orderId: Long, deliveryDate: DateTime): Completable {
        val editOrderDeliveryDateRequestBody = EditOrderDeliveryDateRequestBody(deliveryDate)
        return productsOrdersApi
            .editOrderDeliveryDate(orderId, editOrderDeliveryDateRequestBody)
            .flatMapCompletable {
                when(it.isOrderUpdated){
                    true -> Completable.complete()
                    false -> Completable.error(FailedToUpdateDataInStorageException())
                    else -> Completable.error(ReceivedDataWithWrongFormatException())
                }
            }
            .subscribeOn(ioScheduler)
    }

    private fun List<OrderedProduct>.toRemoteProductsOrder(): List<tv.wfc.livestreamsales.features.rest.model.api.orderproducts.Product>{
        return map {
            tv.wfc.livestreamsales.features.rest.model.api.orderproducts.Product(
                it.product.id,
                it.amount
            )
        }
    }

    private fun RemoteOrder.toApplicationOrder(): Order?{
        val id = this.id ?: return null
        val status = this.status?.toStatus() ?: return null
        val orderDate = this.orderDate ?: return null
        val deliveryDate = this.deliveryDate
        val products = this.products?.mapNotNull { it.toApplicationOrderedProduct() } ?: return null
        val deliveryAddress = this.deliveryAddress?.toApplicationAddress()
        val orderRecipient = this.orderRecipient?.toApplicationOrderRecipient()

        return Order(
            id,
            status,
            orderDate,
            deliveryDate,
            products,
            deliveryAddress,
            orderRecipient
        )
    }

    private fun String.toStatus(): Order.Status?{
        return when(this){
            "created" -> Order.Status.JUST_MADE
            "in_progress" -> Order.Status.IN_PROGRESS
            "done" -> Order.Status.DONE
            else -> null
        }
    }

    private fun RemoteOrderedProduct.toApplicationOrderedProduct(): OrderedProduct?{
        val product = this.product?.toApplicationProduct() ?: return null
        val amount = this.amount ?: 0

        return OrderedProduct(
            product,
            amount
        )
    }

    private fun RemoteProduct.toApplicationProduct(): Product? {
        val id = this.id ?: return null
        val name = this.name ?: return null
        val price = this.price ?: return null

        return Product(
            id,
            name,
            price,
            image = imageUrl
        )
    }

    private fun RemoteAddress.toApplicationAddress(): Address?{
        val city = this.city ?: return null
        val street = this.street ?: return null
        val building = this.building ?: return null
        val flat = this.flat ?: return null

        return Address(
            city,
            street,
            building,
            flat,
            floor
        )
    }

    private fun RemoteOrderRecipient.toApplicationOrderRecipient(): OrderRecipient?{
        val name = this.name ?: return null

        return OrderRecipient(
            name,
            surname,
            email
        )
    }

    private fun Address.toEditOrderAddressRequestBody(): EditOrderAddressRequestBody{
        return EditOrderAddressRequestBody(
            city,
            street,
            building,
            flat,
            floor
        )
    }
}