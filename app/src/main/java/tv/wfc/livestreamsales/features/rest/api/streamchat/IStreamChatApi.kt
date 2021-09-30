package tv.wfc.livestreamsales.features.rest.api.streamchat

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import tv.wfc.livestreamsales.features.rest.api.base.IApi
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageCreationDto
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageCreationResultDto

interface IStreamChatApi: IApi {
    @POST("stream/{stream_id}/chat")
    fun createMessage(
        @Path("stream_id")
        streamId: Long,
        @Body
        message: StreamChatMessageCreationDto
    ): Single<StreamChatMessageCreationResultDto>
}