package tv.wfc.livestreamsales.features.rest.api.authorized.productsorders.models

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("city")
    val city: String?,
    @SerializedName("street")
    val street: String?,
    @SerializedName("building")
    val building: String?,
    @SerializedName("flat")
    val flat: String?,
    @SerializedName("floor")
    val floor: Int?
)