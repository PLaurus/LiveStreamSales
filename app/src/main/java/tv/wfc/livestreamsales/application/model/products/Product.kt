package tv.wfc.livestreamsales.application.model.products

import tv.wfc.livestreamsales.application.model.products.specification.Specification

data class Product(
    val id: Long,
    val name: String,
    val price: Float,
    val quantityInStock: Int? = null,
    val specifications: List<Specification<*>> = emptyList(),
    val oldPrice: Float? = null,
    val description: String? = null,
    val image: String? = null
){
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