package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.models

import com.google.gson.annotations.SerializedName

/**
 * Order data transfer object (DTO) the server sends to client application.
 * Describes the order that was created by client.
 * @param id Unique order identifier.
 * @param status Order status. May be: "created", "paid", "waiting", "done".
 * Created - order is just created;
 * Paid - The order is paid and a shop will process, prepare and deliver it to customer on
 * [deliveryDate];
 * Waiting - ???;
 * Done - The order is delivered to customer.
 * @param orderDate Date and time when the order was created.
 * Date and time is represented as string based on ISO8601 standard.
 * @param deliveryDate Date and time when the order will be delivered to the customer.
 * Date and time is represented as string based on ISO8601 standard.
 * @param products List of ordered products.
 * @param deliveryAddress Delivery address.
 * @param orderRecipient Order recipient. Customer information.
 */
data class Order(
    /**
     * Unique order identifier.
     */
    @SerializedName("id")
    val id: Long?,
    /**
     * Order status. May be: "created", "paid", "waiting", "done".
     * Created - order is just created.
     * Paid - The order is paid and a shop will process, prepare and deliver it to customer on
     * [deliveryDate].
     * Waiting - ???
     * Done - The order is delivered to customer.
     */
    @SerializedName("status")
    val status: String?, // created, paid, waiting, done
    /**
     * Date and time when the order was created.
     * Date and time is represented as string based on ISO8601 standard.
     */
    @SerializedName("order_date")
    val orderDate: String?,
    /**
     * Date and time when the order will be delivered to the customer.
     * Date and time is represented as string based on ISO8601 standard.
     */
    @SerializedName("delivery_date")
    val deliveryDate: String?,
    /**
     * List of ordered products.
     */
    @SerializedName("products")
    val products: List<OrderedProduct>?,
    /**
     * Delivery address.
     */
    @SerializedName("delivery_address")
    val deliveryAddress: Address?,
    /**
     * Order recipient. Customer information.
     */
    @SerializedName("order_recipient")
    val orderRecipient: OrderRecipient?
)