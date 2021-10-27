package tv.wfc.livestreamsales.application.datasource.streamchatmessage.pusher

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.application.pusherfacade.IPusherFacade
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageDto
import javax.inject.Inject

class StreamChatMessagePusherDataSource @Inject constructor(
    private val pusherFacade: IPusherFacade,
    private val streamChatMessageMapper: IEntityMapper<StreamChatMessageDto, StreamChatMessage>,
    @IoScheduler
    private val ioScheduler: Scheduler
): IStreamChatMessagePusherDataSource {
    companion object {
        private const val BASE_STREAM_CHANNEL_NAME = "stream-channel-"
        private const val BASE_NEW_MESSAGE_EVENT_NAME = "stream-event-"
    }

    override fun getNewMessages(streamId: Long): Observable<StreamChatMessage> {
        val streamChannelName = "$BASE_STREAM_CHANNEL_NAME$streamId"
        val newMessageEventName = "$BASE_NEW_MESSAGE_EVENT_NAME$streamId"

        return pusherFacade
            .subscribeOnData(streamChannelName, newMessageEventName, StreamChatMessageDto::class)
            .map { streamChatMessageMapper.map(it) ?: throw ReceivedDataWithWrongFormatException() }
            .subscribeOn(ioScheduler)
    }
}