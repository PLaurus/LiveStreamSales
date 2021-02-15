package tv.wfc.livestreamsales.application.model.productinformation

import com.google.gson.annotations.SerializedName

data class ProductInformation(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("old_price")
    val oldPrice: Float? = null,
    @SerializedName("image")
    val imageUrl: String? = null,
    @SerializedName("available_amount")
    val availableAmount: Int? = null,
    @SerializedName("available_colors")
    val availableColors: Map<Long, String?>? = null, // Long -> color id, String -> Hex color
    @SerializedName("characteristics")
    val characteristics: Map<String, String?>? = null, // 1-st String -> name, 2-nd -> value
)