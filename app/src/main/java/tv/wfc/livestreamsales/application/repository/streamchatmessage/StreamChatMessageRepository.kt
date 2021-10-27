package tv.wfc.livestreamsales.application.repository.streamchatmessage

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.datasource.streamchatmessage.pusher.IStreamChatMessagePusherDataSource
import tv.wfc.livestreamsales.application.datasource.streamchatmessage.rest.IStreamChatMessageRestDataSource
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessageCreationResult
import javax.inject.Inject

class StreamChatMessageRepository @Inject constructor(
    private val streamChatMessageRestDataSource: IStreamChatMessageRestDataSource,
    private val streamChatMessagePusherDataSource: IStreamChatMessagePusherDataSource
): IStreamChatMessageRepository {
    override fun getNewMessages(streamId: Long): Observable<StreamChatMessage> {
        return streamChatMessagePusherDataSource.getNewMessages(streamId)
    }

    override fun createMessage(
        streamId: Long,
        text: String
    ): Single<StreamChatMessageCreationResult> {
        return streamChatMessageRestDataSource.createMessage(streamId, text)
    }
}