package tv.wfc.livestreamsales.application.model.products.order

import tv.wfc.livestreamsales.application.model.products.Product

data class ProductInCart(
    val product: Product,
    val amount: Int
)