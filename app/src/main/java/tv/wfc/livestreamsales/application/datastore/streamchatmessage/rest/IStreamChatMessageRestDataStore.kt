package tv.wfc.livestreamsales.application.datastore.streamchatmessage.rest

import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessageCreationResult

interface IStreamChatMessageRestDataStore {
    fun createMessage(streamId: Long, text: String): Single<StreamChatMessageCreationResult>
}