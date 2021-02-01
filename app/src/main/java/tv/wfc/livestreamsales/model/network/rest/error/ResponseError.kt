package tv.wfc.livestreamsales.model.network.rest.error

import tv.wfc.livestreamsales.model.network.rest.statuscode.HttpStatusCodeType

data class ResponseError(
    val httpStatusCodeType: HttpStatusCodeType,
    val message: String
)