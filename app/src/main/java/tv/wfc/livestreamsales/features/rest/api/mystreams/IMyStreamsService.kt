package tv.wfc.livestreamsales.features.rest.api.mystreams

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamsDto

interface IMyStreamsService : IApi {
    @GET("my-streams")
    fun getMyStreams() : Single<MyStreamsDto>
}