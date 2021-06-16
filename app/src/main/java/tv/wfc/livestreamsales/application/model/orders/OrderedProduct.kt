package tv.wfc.livestreamsales.application.model.orders

import tv.wfc.livestreamsales.application.model.products.Product

data class OrderedProduct(
    val product: Product,
    val amount: Int
)