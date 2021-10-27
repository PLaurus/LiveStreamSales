package tv.wfc.livestreamsales.features.rest.api.mystreams.dto

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class MyStreamDto(
    @SerializedName("private_info")
    val privateInfo: PrivateInfo?,
    @SerializedName("user_id")
    val userId: Long?,
    @SerializedName("wowza_id")
    val wowzaId: String?,
    @SerializedName("public_info")
    val publicInfo: PublicInfo?
) {
    data class PrivateInfo(
        @SerializedName("primary_server")
        val primaryServer: String?,
        @SerializedName("host_port")
        val hostPort: Int?,
        @SerializedName("stream_name")
        val streamName: String?,
        @SerializedName("disable_authentication")
        val disableAuthentication: Boolean?,
        @SerializedName("username")
        val userName: String?,
        @SerializedName("password")
        val password: String?
    )

    data class PublicInfo(
        @SerializedName("id")
        val id: Long?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("start_at")
        val startAt: DateTime?,
        @SerializedName("end_at")
        val endAt: DateTime?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("image")
        val image: String?,
        @SerializedName("manifest")
        val manifest: String?,
        @SerializedName("products")
        val products: List<Product?>?
    ) {
        data class Product(
            @SerializedName("name")
            val name: String?,
            @SerializedName("image")
            val image: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("brand")
            val brand: String?,
            @SerializedName("skus")
            val skus: List<Sku>?
        ) {
            data class Sku(
                @SerializedName("id")
                val id: Long,
                @SerializedName("in_stock")
                val inStock: Int?,
                @SerializedName("price")
                val price: Float?,
                @SerializedName("properties")
                val properties: List<Property>?
            ) {
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
            }
        }
    }
}