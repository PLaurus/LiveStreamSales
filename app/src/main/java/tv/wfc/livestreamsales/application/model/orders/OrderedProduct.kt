package tv.wfc.livestreamsales.application.model.orders

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import tv.wfc.livestreamsales.application.model.products.Product

@Entity(
    tableName = "cart",
    primaryKeys = ["product_id"]
)
data class OrderedProduct(
    /**
     * Product that is ordered.
     */
    @Embedded(prefix = "product_")
    val product: Product,
    /**
     * Amount of ordered products.
     */
    @ColumnInfo(name = "amount")
    val amount: Int
)