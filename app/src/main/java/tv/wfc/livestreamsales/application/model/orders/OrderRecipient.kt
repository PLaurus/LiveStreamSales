package tv.wfc.livestreamsales.application.model.orders

data class OrderRecipient(
    val name: String,
    val surname: String? = null,
    val email: String? = null
)