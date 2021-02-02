package tv.wfc.livestreamsales.network.rest.api.notauthorized

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import tv.wfc.livestreamsales.model.application.broadcastinformation.BroadcastBaseInformation
import tv.wfc.livestreamsales.network.rest.api.base.IApi

interface IBroadcastsInformationApi: IApi {
    @GET("stream")
    fun getBroadcasts(): Single<Response<List<BroadcastBaseInformation>>>
}