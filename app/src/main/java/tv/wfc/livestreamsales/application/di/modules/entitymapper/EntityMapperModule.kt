package tv.wfc.livestreamsales.application.di.modules.entitymapper

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.application.model.stream.mapper.*
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessageCreationResult
import tv.wfc.livestreamsales.application.model.streamchatmessage.mapper.StreamChatMessageCreationResultDtoMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.mapper.StreamChatMessageDtoMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.mapper.StreamChatMessageDtoUserDtoMapper
import tv.wfc.livestreamsales.application.model.streamchatmessage.mapper.StreamChatStringMessageMapper
import tv.wfc.livestreamsales.application.model.streamingservice.StreamingService
import tv.wfc.livestreamsales.application.model.streamingservice.mapper.MyStreamDtoToStreamingServiceAuthenticationDataMapper
import tv.wfc.livestreamsales.application.model.streamingservice.mapper.MyStreamDtoToStreamingServiceMapper
import tv.wfc.livestreamsales.application.model.streamingservice.mapper.StreamDtoToStreamingServiceAuthenticationDataMapper
import tv.wfc.livestreamsales.application.model.streamingservice.mapper.StreamDtoToStreamingServiceMapper
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamDto
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamsDto
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamCreationResultDto
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamDto
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamUpdateResultDto
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageCreationResultDto
import tv.wfc.livestreamsales.features.rest.api.streamchat.dto.StreamChatMessageDto

@Module
interface EntityMapperModule {
    @Binds
    fun bindStreamChatMessageCreationResultDtoMapper(
        mapper: StreamChatMessageCreationResultDtoMapper
    ): IEntityMapper<StreamChatMessageCreationResultDto, StreamChatMessageCreationResult>

    @Binds
    fun bindStreamChatMessageDtoMapper(
        mapper: StreamChatMessageDtoMapper
    ): IEntityMapper<StreamChatMessageDto, StreamChatMessage>

    @Binds
    fun bindStreamChatMessageDtoUserDtoMapper(
        mapper: StreamChatMessageDtoUserDtoMapper
    ): IEntityMapper<StreamChatMessageDto.UserDto, StreamChatMessage.Sender>

    @Binds
    fun bindStreamChatStringMessageMapper(
        mapper: StreamChatStringMessageMapper
    ): IEntityMapper<String, StreamChatMessage>

    @Binds
    fun bindStreamDtoToStreamManagementMapper(
        mapper: StreamDtoToMyStreamMapper
    ): IEntityMapper<StreamDto, MyStream>

    @Binds
    fun bindStreamDtoToStreamingServiceAuthenticationDataMapper(
        mapper: StreamDtoToStreamingServiceAuthenticationDataMapper
    ): IEntityMapper<StreamDto, StreamingService.AuthenticationData>

    @Binds
    fun bindStreamDtoToStreamingServiceMapper(
        mapper: StreamDtoToStreamingServiceMapper
    ): IEntityMapper<StreamDto, StreamingService>

    @Binds
    fun bindStreamCreationResultDtoToMyStreamMapper(
        mapper: StreamCreationResultDtoToMyStreamMapper
    ): IEntityMapper<StreamCreationResultDto, MyStream>

    @Binds
    fun bindStreamUpdateResultDtoToMyStreamMapper(
        mapper: StreamUpdateResultDtoToMyStreamMapper
    ): IEntityMapper<StreamUpdateResultDto, MyStream>

    @Binds
    fun bindMyStreamsDtoToListOfMyStreamsMapper(
        mapper: MyStreamsDtoToListOfMyStreamsMapper
    ): IEntityMapper<MyStreamsDto, List<MyStream>>

    @Binds
    fun bindMyStreamDtoToMyStreamMapper(
        mapper: MyStreamDtoToMyStreamMapper
    ): IEntityMapper<MyStreamDto, MyStream>

    @Binds
    fun bindMyStreamDtoToStreamingServiceMapper(
        mapper: MyStreamDtoToStreamingServiceMapper
    ): IEntityMapper<MyStreamDto, StreamingService>

    @Binds
    fun bindMyStreamDtoToStreamingServiceAuthenticationDataMapper(
        mapper: MyStreamDtoToStreamingServiceAuthenticationDataMapper
    ): IEntityMapper<MyStreamDto, StreamingService.AuthenticationData>
}