package tv.wfc.livestreamsales.application.datastore.streamchatmessage.rest

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessageCreationResult
import tv.wfc.livestreamsales.application.model.exception.storage.ReceivedDataWithWrongFormatException
import tv.wfc.livestreamsales.features.rest.api.streamchat.IStreamChatApi
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageCreationDto
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageCreationResultDto
import javax.inject.Inject

class StreamChatMessageRestDataStore @Inject constructor(
    private val streamChatApi: IStreamChatApi,
    private val streamChatMessageCreationResultMapper: IEntityMapper<StreamChatMessageCreationResultDto, StreamChatMessageCreationResult>,
    @IoScheduler
    private val ioScheduler: Scheduler
): IStreamChatMessageRestDataStore {
    override fun createMessage(streamId: Long, text: String): Single<StreamChatMessageCreationResult> {
        val streamChatMessageCreationDto = StreamChatMessageCreationDto(text)

        return streamChatApi
            .createMessage(streamId, streamChatMessageCreationDto)
            .map{ streamChatMessageCreationResultMapper.mapToDomainEntity(it) ?: throw ReceivedDataWithWrongFormatException() }
            .subscribeOn(ioScheduler)
    }
}