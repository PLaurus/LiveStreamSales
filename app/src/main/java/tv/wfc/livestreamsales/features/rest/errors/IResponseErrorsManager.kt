package tv.wfc.livestreamsales.features.rest.errors

import tv.wfc.livestreamsales.features.rest.model.ResponseError
import io.reactivex.rxjava3.core.Observable

interface IResponseErrorsManager {
    val errors: Observable<ResponseError>
    fun checkResponseStatus(statusCode: Int, message: String)
}