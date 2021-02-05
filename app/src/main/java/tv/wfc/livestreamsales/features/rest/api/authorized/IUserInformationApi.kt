package tv.wfc.livestreamsales.features.rest.api.authorized

import tv.wfc.livestreamsales.features.rest.model.api.request.UpdateUserInformationRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.response.GetUserInformationResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.response.UpdateUserInformationResponseBody
import tv.wfc.livestreamsales.features.rest.api.base.IAuthorizedApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IUserInformationApi: IAuthorizedApi {
    @GET("profile")
    fun getUserInformation(): Single<Response<GetUserInformationResponseBody>>

    @POST("profile")
    fun updateUserInformation(
        @Body updateUserInformationRequestBody: UpdateUserInformationRequestBody
    ): Single<Response<UpdateUserInformationResponseBody>>
}