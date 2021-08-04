package tv.wfc.livestreamsales.application.model.orders

import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.model.address.Address

data class Order(
    val id: Long,
    val status: Status,
    val orderDate: DateTime,
    val deliveryDate: DateTime?,
    val products: List<OrderedProduct>,
    val deliveryAddress: Address?,
    val orderRecipient: OrderRecipient?
){
    val orderPrice = calculateOrderPrice()

    enum class Status{
        CREATED,
        PAID,
        WAITING,
        DONE
    }

    private fun calculateOrderPrice(): Float{
        return products
            .map{ it.product.price * it.amount }
            .reduceOrNull{ accumulated, next -> accumulated + next } ?: 0f
    }
}