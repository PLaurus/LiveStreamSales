package tv.wfc.livestreamsales.application.database.tables

import androidx.room.*
import androidx.room.rxjava3.EmptyResultSetException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.Product

@Dao
abstract class CartDao: BaseDao<OrderedProduct>() {
    /**
     * Gets all [OrderedProduct]s from the cart synchronously.
     * @return List of all [OrderedProduct]s in the cart.
     */
    @Query("SELECT * FROM cart")
    abstract fun getAll(): List<OrderedProduct>

    /**
     * Gets all [OrderedProduct]s from the cart reactively one time.
     * @return List of all [OrderedProduct]s in the cart.
     */
    @Query("SELECT * FROM cart")
    abstract fun rxGetAllSingle(): Single<List<OrderedProduct>>

    /**
     * Gets all [OrderedProduct]s from the cart reactively continuously.
     * @return List of all [OrderedProduct]s in the cart.
     */
    @Query("SELECT * FROM cart")
    abstract fun rxGetAll(): Observable<List<OrderedProduct>>

    /**
     * Gets the first [OrderedProduct] entry with the [productId] synchronously.
     * @return The first [OrderedProduct] entry with the [productId].
     */
    @Query("SELECT * FROM cart WHERE product_id = :productId LIMIT 1")
    abstract fun get(productId: Long): OrderedProduct?

    /**
     * Gets the first [OrderedProduct] entry with the [productId] reactively.
     * Single will trigger [EmptyResultSetException] if query return no rows.
     * @return The first [OrderedProduct] entry with the [productId].
     */
    @Query("SELECT * FROM cart WHERE product_id = :productId LIMIT 1")
    abstract fun rxGet(productId: Long): Single<OrderedProduct>

    /**
     * Deletes an [OrderedProduct] with the [productId] from the cart reactively.
     */
    @Query("DELETE FROM cart WHERE product_id = :productId")
    abstract fun delete(productId: Long)

    /**
     * Deletes an [OrderedProduct] with the [productId] from the cart reactively.
     */
    @Query("DELETE FROM cart WHERE product_id = :productId")
    abstract fun rxDelete(productId: Long): Completable

    /**
     * Deletes all [OrderedProduct]s from the cart synchronously.
     */
    @Query("DELETE FROM cart")
    abstract fun deleteAll()

    /**
     * Deletes all [OrderedProduct]s from the cart reactively.
     */
    @Query("DELETE FROM cart")
    abstract fun rxDeleteAll(): Completable

    /**
     * Adds [amount] of [product]s to the cart synchronously.
     * If [amount] is less then zero method will add [amount] equal to 1.
     */
    @Transaction
    open fun add(product: Product, amount: Int = 1){
        val additionalAmount = amount.coerceAtLeast(1)
        val existingOrderedProduct = get(product.id)

        if(existingOrderedProduct != null){
            val newAmount = existingOrderedProduct.amount + additionalAmount
            val updatedOrderedProduct = OrderedProduct(existingOrderedProduct.product, newAmount)
            update(updatedOrderedProduct)
        } else{
            val updatedOrderedProduct = OrderedProduct(product, additionalAmount)
            insert(updatedOrderedProduct)
        }
    }

    /**
     * Adds [amount] of [product]s to the cart reactively.
     * If [amount] is less then zero method will add [amount] equal to 1.
     */
    fun rxAdd(product: Product, amount: Int = 1): Completable{
        return Completable.fromRunnable {
            add(product, amount)
        }
    }

    /**
     * Removes at all (if [amount] is null) or removes [amount] units of the product
     * with [productId] from the cart synchronously.
     * In another words it will update [OrderedProduct] entry's amount field with
     * amount - [amount] or delete the entry at all if new amount is less then zero.
     * If [amount] is null (it is null by default) product associated with [productId]
     * will be deleted.
     * If [amount] is less then zero method will remove [amount] equal to 1.
     */
    @Transaction
    open fun remove(productId: Long, amount: Int? = null){
        val amountToRemove = amount?.coerceAtLeast(1)

        if(amountToRemove == null){
            delete(productId)
            return
        }

        val existingOrderedProduct = get(productId) ?: return

        val newAmount = existingOrderedProduct.amount - amountToRemove

        if(newAmount <= 0) {
            delete(productId)
        } else{
            val updatedOrderedProduct = OrderedProduct(existingOrderedProduct.product, newAmount)
            update(updatedOrderedProduct)
        }
    }

    /**
     * Removes at all (if [amount] is null) or removes [amount] [product]s from the cart
     * synchronously. In another words it will update [OrderedProduct] entry's amount field
     * with amount - [amount] or delete the entry at all if new amount is less then zero.
     * If [amount] is null (it is null by default) [product] will be deleted.
     * If [amount] is less then zero method will remove [amount] equal to 1.
     */
    fun remove(product: Product, amount: Int? = null){
        remove(product.id, amount)
    }

    /**
     * Removes at all (if [amount] is null) or removes [amount] units of the products
     * with [productId] from the cart reactively.
     * In another words it will update [OrderedProduct] entry's amount field with
     * amount - [amount] or delete the entry at all if new amount is less then zero.
     * * If [amount] is null (it is null by default) product associated with [productId]
     * will be deleted.
     * If [amount] is less then zero method will remove [amount] equal to 1.
     */
    fun rxRemove(productId: Long, amount: Int? = null): Completable{
        return Completable.fromRunnable {
            remove(productId, amount)
        }
    }

    /**
     * Removes at all (if [amount] is null) or removes [amount] [product]s from the cart reactively.
     * In another words it will update [OrderedProduct] entry's amount field with
     * amount - [amount] or delete the entry at all if new amount is less then zero.
     * If [amount] is null (it is null by default) [product] will be deleted.
     * If [amount] is less then zero method will remove [amount] equal to 1.
     */
    fun rxRemove(product: Product, amount: Int = 1): Completable{
        return Completable.fromRunnable {
            remove(product, amount)
        }
    }
}