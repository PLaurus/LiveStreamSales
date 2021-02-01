package tv.wfc.livestreamsales.network.rest.errors

import tv.wfc.livestreamsales.model.network.rest.error.ResponseError
import tv.wfc.livestreamsales.model.network.rest.statuscode.HttpStatusCodeType
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class ResponseErrorsManager @Inject constructor(): IResponseErrorsManager {
    private val errorsSubject: PublishSubject<ResponseError> = PublishSubject.create()
    override val errors: Observable<ResponseError> = errorsSubject.hide()

    override fun checkResponseStatus(statusCode: Int, message: String){
        val httpStatusCodeType = HttpStatusCodeType.fromInt(statusCode)

        if(httpStatusCodeType != HttpStatusCodeType.Success){
            val error = ResponseError(httpStatusCodeType, message)
            errorsSubject.onNext(error)
        }
    }
}