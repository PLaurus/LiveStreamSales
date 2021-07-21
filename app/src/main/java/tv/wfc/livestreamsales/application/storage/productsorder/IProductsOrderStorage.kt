package tv.wfc.livestreamsales.application.storage.productsorder

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime
import tv.wfc.livestreamsales.application.model.address.Address
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.Product
import tv.wfc.livestreamsales.application.model.storage.StorageDataUpdateResult

interface IProductsOrderStorage {
    /**
     * Adds [amount] of [product]s to the cart.
     * If [amount] is less then zero method will add [amount] equal to 1.
     */
    fun addProductToCart(product: Product, amount: Int = 1): Completable

    /**
     * Adds one more unit of each product from [products] to the cart.
     */
    fun addProductsToCart(products: List<Product>): Completable

    /**
     * Gets all [OrderedProduct]s from the cart one time.
     */
    fun getOrderedProductsFromCartSingle(): Single<List<OrderedProduct>>

    /**
     * Gets all [OrderedProduct]s from the cart continuously.
     */
    fun getOrderedProductsFromCart(): Observable<List<OrderedProduct>>

    /**
     * Removes [amount] units of the [product] from the cart or delete the entry at all if new
     * amount is less then zero.
     * If [amount] is less then zero method will remove [amount] equal to 1.
     */
    fun removeProductFromCart(product: Product, amount: Int = 1): Completable

    /**
     * Removes [amount] units of the product associated with the [productId] from the cart
     * or delete the entry at all if new amount is less then zero.
     * If [amount] is less then zero method will remove [amount] equal to 1.
     */
    fun removeProductFromCart(productId: Long, amount: Int = 1): Completable

    /**
     * Removes all [product] units from cart.
     * For example, if there is amount = 8 for [product] it doesn't matter for the method and
     * it just removes entry at all.
     */
    fun removeAllProductUnitsFromCart(product: Product): Completable

    /**
     * Removes all product units associated with the [productId] from the cart.
     * For example, if there is amount = 8 for product with associated [productId] it doesn't
     * matter for the method and it just removes entry at all.
     */
    fun removeAllProductUnitsFromCart(productId: Long): Completable

    /**
     * Removes all [products]' units from the cart.
     */
    fun removeAllProductsUnitsFromCart(products: List<Product>): Completable

    /**
     * Removes all products' units with associated [productIds] from the cart.
     */
    fun removeAllProductsUnitsFromCart(vararg productIds: Long): Completable

    /**
     * Removes all products from the cart.
     */
    fun removeAllProductsUnitsFromCart(): Completable

    /**
     * Sends request to order [products] to remote server. This method shouldn't be implemented
     * for local storage.
     */
    fun orderProducts(products: List<OrderedProduct>): Completable

    fun getOrders(): Single<List<Order>>
    fun getOrder(id: Long): Single<Order>
    fun confirmOrder(
        orderId: Long,
        deliveryAddress: Address,
        deliveryDate: DateTime
    ): Single<StorageDataUpdateResult>
}