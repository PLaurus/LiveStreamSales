package tv.wfc.livestreamsales.features.rest.model.api.getorders

import com.google.gson.annotations.SerializedName

data class OrderRecipient(
    @SerializedName("name")
    val name: String?,
    @SerializedName("surname")
    val surname: String?,
    @SerializedName("email")
    val email: String?
)