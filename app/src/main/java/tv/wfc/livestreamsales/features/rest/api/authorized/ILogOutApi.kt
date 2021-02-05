package tv.wfc.livestreamsales.features.rest.api.authorized

import tv.wfc.livestreamsales.features.rest.api.base.IAuthorizedApi
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.POST

interface ILogOutApi: IAuthorizedApi {
    @POST("logout")
    fun logOut(): Single<Response<Unit>>
}