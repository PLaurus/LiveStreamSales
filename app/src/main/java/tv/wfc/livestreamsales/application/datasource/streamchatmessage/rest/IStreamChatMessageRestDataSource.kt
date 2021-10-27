package tv.wfc.livestreamsales.application.datasource.streamchatmessage.rest

import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessageCreationResult

interface IStreamChatMessageRestDataSource {
    fun createMessage(streamId: Long, text: String): Single<StreamChatMessageCreationResult>
}