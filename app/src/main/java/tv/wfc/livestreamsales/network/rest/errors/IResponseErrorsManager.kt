package tv.wfc.livestreamsales.network.rest.errors

import tv.wfc.livestreamsales.model.network.rest.error.ResponseError
import io.reactivex.rxjava3.core.Observable

interface IResponseErrorsManager {
    val errors: Observable<ResponseError>
    fun checkResponseStatus(statusCode: Int, message: String)
}