package tv.wfc.livestreamsales.application.repository.streamchatmessage

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessageCreationResult

interface IStreamChatMessageRepository {
    /**
     * Gets observable that notifies subscribers about new messages on IO thread
     */
    fun getNewMessages(streamId: Long): Observable<StreamChatMessage>
    fun createMessage(streamId: Long, text: String): Single<StreamChatMessageCreationResult>
}