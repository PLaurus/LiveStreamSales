package tv.wfc.livestreamsales.application.model.products

import androidx.room.ColumnInfo
import androidx.room.Ignore
import tv.wfc.livestreamsales.application.model.products.specification.Specification

data class Product @Ignore constructor(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Float,
    @Ignore
    val specifications: List<Specification<*>>,
    @ColumnInfo(name = "quantity_in_stock")
    val quantityInStock: Int? = null,
    @ColumnInfo(name = "old_price")
    val oldPrice: Float? = null,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "image")
    val image: String? = null
){
    constructor(id: Long,
                name: String,
                price: Float,
                quantityInStock: Int? = null,
                oldPrice: Float? = null,
                description: String? = null,
                image: String? = null
    ) : this(id, name, price, emptyList(), quantityInStock, oldPrice, description, image)

    companion object{
        fun create(productGroup: ProductGroup, productId: Long): Product?{

            productGroup.productVariants.find{ it.id == productId }?.let{ productVariant ->
                val allSpecifications: List<Specification<*>> = mutableListOf<Specification<*>>().apply {
                    addAll(productGroup.specifications)
                    addAll(productVariant.specifications)
                }

                return Product(
                    id = productId,
                    name = productGroup.name,
                    price = productVariant.price,
                    quantityInStock = productVariant.quantityInStock,
                    specifications = allSpecifications,
                    oldPrice = productVariant.oldPrice,
                    description = productGroup.description,
                    image = productGroup.image
                )
            }

            return null
        }
    }
}