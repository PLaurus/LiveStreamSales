package tv.wfc.livestreamsales.features.rest.api.authorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tv.wfc.livestreamsales.features.rest.api.base.IAuthorizedApi
import tv.wfc.livestreamsales.features.rest.model.api.request.UpdateUserPersonalInformationRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.response.GetUserPersonalInformationResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.response.UpdateUserInformationResponseBody

interface IUserPersonalInformationApi: IAuthorizedApi {
    @GET("profile")
    fun getUserPersonalInformation(): Single<GetUserPersonalInformationResponseBody>

    @POST("profile")
    fun updateUserPersonalInformation(
        @Body updateUserPersonalInformationRequestBody: UpdateUserPersonalInformationRequestBody
    ): Single<UpdateUserInformationResponseBody>
}