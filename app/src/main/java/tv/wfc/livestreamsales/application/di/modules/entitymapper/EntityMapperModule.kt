package tv.wfc.livestreamsales.application.di.modules.entitymapper

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.*
import tv.wfc.livestreamsales.application.model.streamchatmessage.mapper.StreamChatMessageCreationResultDtoMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.mapper.StreamChatMessageDtoMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.mapper.StreamChatMessageDtoUserDtoMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.mapper.StreamChatStringMessageMapper
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageCreationResultDto
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageDto

@Module
abstract class EntityMapperModule {
    @Binds
    internal abstract fun bindStreamChatMessageCreationResultDtoMapper(
        mapper: StreamChatMessageCreationResultDtoMapper
    ): IEntityMapper<StreamChatMessageCreationResultDto, StreamChatMessageCreationResult>

    @Binds
    internal abstract fun bindStreamChatMessageDtoMapper(
        mapper: StreamChatMessageDtoMapper
    ): IEntityMapper<StreamChatMessageDto, StreamChatMessage>

    @Binds
    internal abstract fun bindStreamChatMessageDtoUserDtoMapper(
        mapper: StreamChatMessageDtoUserDtoMapper
    ): IEntityMapper<StreamChatMessageDto.UserDto, StreamChatMessage.Sender>

    @Binds
    internal abstract fun bindStreamChatStringMessageMapper(
        mapper: StreamChatStringMessageMapper
    ): IEntityMapper<String, StreamChatMessage>
}