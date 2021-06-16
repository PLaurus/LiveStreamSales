package tv.wfc.livestreamsales.features.rest.model.api.getorders

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class Order(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("status")
    val status: String?, // created, in_progress, done
    @SerializedName("order_date")
    val orderDate: DateTime?,
    @SerializedName("delivery_date")
    val deliveryDate: DateTime?,
    @SerializedName("products")
    val products: List<OrderedProduct>?,
    @SerializedName("delivery_address")
    val deliveryAddress: Address?,
    @SerializedName("order_recipient")
    val orderRecipient: OrderRecipient?
)