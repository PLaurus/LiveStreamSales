package tv.wfc.livestreamsales.features.rest.model.api.orderproducts

import com.google.gson.annotations.SerializedName

data class Property(
    @SerializedName("type")
    val type: String?, // color, regular
    @SerializedName("name")
    val name: String?,
    @SerializedName("value")
    val value: String?,
    @SerializedName("hex")
    val colorHex: String? // #000000, ...
)