package tv.wfc.livestreamsales.features.rest.api.authorized

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tv.wfc.livestreamsales.features.rest.api.base.IAuthorizedApi
import tv.wfc.livestreamsales.features.rest.model.api.request.NotifyWatchingBroadcastRequestBody

interface IBroadcastAnalyticsApi: IAuthorizedApi {
    @POST("notify_watching_broadcast")
    fun notifyWatchingBroadcast(
        @Body
        notifyWatchingBroadcastRequestBody: NotifyWatchingBroadcastRequestBody
    ): Single<Response<Unit>>
}