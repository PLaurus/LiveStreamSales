package tv.wfc.livestreamsales.application.datasource.streamchatmessage.pusher

import io.reactivex.rxjava3.core.Observable
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage

interface IStreamChatMessagePusherDataSource {
    /**
     * Gets observable that notifies subscribers about new messages on IO thread.
     */
    fun getNewMessages(streamId: Long): Observable<StreamChatMessage>
}