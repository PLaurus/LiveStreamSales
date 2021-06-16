package tv.wfc.livestreamsales.application.model.address

data class Address(
    val city: String,
    val street: String,
    val building: String,
    val flat: String,
    val floor: Int?
)