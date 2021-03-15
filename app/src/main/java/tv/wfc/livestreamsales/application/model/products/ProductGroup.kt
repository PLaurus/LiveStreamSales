package tv.wfc.livestreamsales.application.model.products

import tv.wfc.livestreamsales.application.model.products.specification.Specification

data class ProductGroup(
    val name: String,
    val productVariants: List<ProductVariant>,
    val image: String? = null,
    val description: String? = null,
    val specifications: List<Specification<*>> = emptyList()
)