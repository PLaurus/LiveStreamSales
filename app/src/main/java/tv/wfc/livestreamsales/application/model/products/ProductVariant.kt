package tv.wfc.livestreamsales.application.model.products

import tv.wfc.livestreamsales.application.model.products.specification.Specification

data class ProductVariant(
    val id: Long,
    val quantityInStock: Int,
    val price: Float,
    val oldPrice: Float? = null,
    val specifications: List<Specification<*>>
)