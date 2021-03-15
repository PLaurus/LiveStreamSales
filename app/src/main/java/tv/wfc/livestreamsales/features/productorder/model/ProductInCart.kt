package tv.wfc.livestreamsales.features.productorder.model

import tv.wfc.livestreamsales.application.model.products.Product

data class ProductInCart(
    val product: Product,
    val amount: Int
)