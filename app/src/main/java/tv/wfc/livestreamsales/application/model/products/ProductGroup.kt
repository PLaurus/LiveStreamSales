package tv.wfc.livestreamsales.application.model.products

import tv.wfc.livestreamsales.application.model.products.specification.Specification

data class ProductGroup(
    val name: String,
    val productVariants: List<ProductVariant>,
    val image: String? = null,
    val description: String? = null,
    val specifications: List<Specification<*>> = emptyList()
){
    /**
     * Quantity of all product product group contains in stock
     */
    val quantityInStock = productVariants
        .map { it.quantityInStock }
        .reduceOrNull{ result, next -> result + next}

    val minProductVariantPrice = productVariants
        .map{ it.price }
        .minOrNull()

    fun toProducts(): List<Product> {
        val products = mutableListOf<Product>()

        productVariants.forEach { productVariant ->
            val productId = productVariant.id
            Product.create(this, productId)?.let{ product ->
                products.add(product)
            }
        }

        return products
    }
}