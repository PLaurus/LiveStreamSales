package tv.wfc.livestreamsales.network.rest.api.notauthorized

import tv.wfc.livestreamsales.model.network.rest.request.ConfirmPhoneNumberRequestBody
import tv.wfc.livestreamsales.model.network.rest.request.SendConfirmationCodeRequestRequestBody
import tv.wfc.livestreamsales.model.network.rest.response.ConfirmPhoneNumberResponseBody
import tv.wfc.livestreamsales.model.network.rest.response.GetConfirmationCodeLengthResponseBody
import tv.wfc.livestreamsales.model.network.rest.response.GetNextCodeRequestRequiredWaitingTimeResponseBody
import tv.wfc.livestreamsales.model.network.rest.response.SendCodeResponseBody
import tv.wfc.livestreamsales.network.rest.api.base.IApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ILogInApi: IApi {
    @POST("login")
    fun sendConfirmationCodeRequest(
        @Body sendConfirmationCodeRequestRequestBody: SendConfirmationCodeRequestRequestBody
    ): Single<Response<SendCodeResponseBody>>

    @POST("sms")
    fun confirmPhoneNumber(
        @Body confirmPhoneNumberRequestBody: ConfirmPhoneNumberRequestBody
    ): Single<Response<ConfirmPhoneNumberResponseBody>>

    @GET("confirmation_code_length")
    fun getConfirmationCodeLength(): Single<Response<GetConfirmationCodeLengthResponseBody>>

    @GET("next_code_request_required_waiting_time")
    fun getNextCodeRequestRequiredWaitingTime(): Single<Response<GetNextCodeRequestRequiredWaitingTimeResponseBody>>
}