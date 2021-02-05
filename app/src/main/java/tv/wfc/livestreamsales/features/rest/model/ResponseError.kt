package tv.wfc.livestreamsales.features.rest.model

data class ResponseError(
    val httpStatusCodeType: HttpStatusCodeType,
    val message: String
)