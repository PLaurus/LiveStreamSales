package tv.wfc.livestreamsales.features.rest.api.mystreams.dto

import com.google.gson.annotations.SerializedName

data class MyStreamDto(
    @SerializedName("private_info")
    val privateInfo: PrivateInfo?,
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

    /**
     * Public stream information.
     * @param id The stream unique identifier.
     * @param name The stream name.
     * @param startAt Date and time when the stream comes online.
     * Date and time is represented as string based on ISO8601 standard.
     * @param endAt Date and time when stream goes offline.
     * Date and time is represented as string based on ISO8601 standard.
     * @param description The stream description.
     * @param image URL of stream preview image.
     * @param manifest The stream m3u8 manifest.
     * @param products List of products that are offered for buying for stream viewers (customers).
     * @param userId Streamer unique identifier.
     */
    data class PublicInfo(
        /**
         * The stream unique identifier.
         */
        @SerializedName("id")
        val id: Long?,
        /**
         * The stream name.
         */
        @SerializedName("name")
        val name: String?,
        /**
         * Date and time when the stream comes online.
         * Date and time is represented as string based on ISO8601 standard.
         */
        @SerializedName("start_at")
        val startAt: String?,
        /**
         * Date and time when stream goes offline.
         * Date and time is represented as string based on ISO8601 standard.
         */
        @SerializedName("end_at")
        val endAt: String?,
        /**
         * The stream description.
         */
        @SerializedName("description")
        val description: String?,
        /**
         * URL of stream preview image.
         */
        @SerializedName("image")
        val image: String?,
        /**
         * The stream m3u8 manifest.
         */
        @SerializedName("manifest")
        val manifest: String?,
        /**
         * List of products that are offered for buying for stream viewers (customers).
         */
        @SerializedName("products")
        val products: List<Product?>?,
        /**
         * Streamer unique identifier.
         */
        @SerializedName("user_id")
        val userId: Long?
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