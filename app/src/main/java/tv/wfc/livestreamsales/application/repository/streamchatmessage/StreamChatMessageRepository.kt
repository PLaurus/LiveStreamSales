package tv.wfc.livestreamsales.application.repository.streamchatmessage

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.datastore.streamchatmessage.pusher.IStreamChatMessagePusherDataStore
import tv.wfc.livestreamsales.application.datastore.streamchatmessage.rest.IStreamChatMessageRestDataStore
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessageCreationResult
import javax.inject.Inject

class StreamChatMessageRepository @Inject constructor(
    private val streamChatMessageRestDataStore: IStreamChatMessageRestDataStore,
    private val streamChatMessagePusherDataStore: IStreamChatMessagePusherDataStore
): IStreamChatMessageRepository {
    override fun getNewMessages(streamId: Long): Observable<StreamChatMessage> {
        return streamChatMessagePusherDataStore.getNewMessages(streamId)
    }

    override fun createMessage(
        streamId: Long,
        text: String
    ): Single<StreamChatMessageCreationResult> {
        return streamChatMessageRestDataStore.createMessage(streamId, text)
    }
}