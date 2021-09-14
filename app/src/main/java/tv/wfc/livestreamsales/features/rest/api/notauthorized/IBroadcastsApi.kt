package tv.wfc.livestreamsales.features.rest.api.notauthorized

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts.GetBroadcastResponseBody
import tv.wfc.livestreamsales.features.rest.model.api.getbroadcasts.GetBroadcastsResponseBody

interface IBroadcastsApi: IApi {
    @GET("stream")
    fun getBroadcasts(): Single<GetBroadcastsResponseBody>

    @GET("stream/{id}")
    fun getBroadcast(
        @Path("id")
        id: Long
    ): Single<GetBroadcastResponseBody>

    @GET("stream/{id}/viewers")
    fun getBroadcastViewersCount(
        @Path("id")
        broadcastId: Long
    ): Single<Int>
}