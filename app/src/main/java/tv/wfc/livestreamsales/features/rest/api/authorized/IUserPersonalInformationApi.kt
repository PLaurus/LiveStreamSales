package tv.wfc.livestreamsales.features.rest.api.authorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.model.api.getuserpersonalinformation.GetUserPersonalInformationResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.updateuserpersonalinformation.UpdateUserPersonalInformationRequestBody
import tv.wfc.livestreamsales.features.rest.model.api.updateuserpersonalinformation.UpdateUserPersonalInformationResponseBody

interface IUserPersonalInformationApi: IApi {
    @GET("profile")
    fun getUserPersonalInformation(): Single<GetUserPersonalInformationResponseBody>

    @POST("profile")
    fun updateUserPersonalInformation(
        @Body updateUserPersonalInformationRequestBody: UpdateUserPersonalInformationRequestBody
    ): Single<UpdateUserPersonalInformationResponseBody>
}