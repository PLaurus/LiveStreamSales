package tv.wfc.livestreamsales.features.rest.api.notauthorized

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.model.api.response.GetBroadcastResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.response.GetBroadcastViewersCountResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.response.GetBroadcastsResponseBody

interface IBroadcastsInformationApi: IApi {
    @GET("stream")
    fun getBroadcasts(): Single<Response<GetBroadcastsResponseBody>>

    @GET("stream/{id}")
    fun getBroadcast(
        @Path("id")
        id: Long
    ): Single<Response<GetBroadcastResponseBody>>

    @GET("stream/{id}")
    fun getBroadcastViewersCount(
        @Path("id")
        broadcastId: Long
    ): Single<Response<GetBroadcastViewersCountResponseBody>>
}