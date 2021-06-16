package tv.wfc.livestreamsales.features.rest.api.notauthorized

import tv.wfc.livestreamsales.features.rest.model.api.confirmphonenumber.ConfirmPhoneNumberRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.sendconfirmationcoderequest.SendConfirmationCodeRequestRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.confirmphonenumber.ConfirmPhoneNumberResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.getconfirmationcodelength.GetConfirmationCodeLengthResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.getnextcoderequestrequiredwaitingtime.GetNextCodeRequestRequiredWaitingTimeResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.sendconfirmationcoderequest.SendConfirmationCodeResponseBody
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IAuthorizationApi: IApi {
    @POST("login")
    fun sendConfirmationCodeRequest(
        @Body sendConfirmationCodeRequestRequestBody: SendConfirmationCodeRequestRequestBody
    ): Single<Response<SendConfirmationCodeResponseBody>>

    @POST("sms")
    fun confirmPhoneNumber(
        @Body confirmPhoneNumberRequestBody: ConfirmPhoneNumberRequestBody
    ): Single<Response<ConfirmPhoneNumberResponseBody>>

    @GET("confirmation_code_length")
    fun getConfirmationCodeLength(): Single<Response<GetConfirmationCodeLengthResponseBody>>

    @GET("next_code_request_required_waiting_time")
    fun getNextCodeRequestRequiredWaitingTime(): Single<Response<GetNextCodeRequestRequiredWaitingTimeResponseBody>>

    @POST("logout")
    fun logOut(): Single<Response<Unit>>
}