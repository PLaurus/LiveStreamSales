package tv.wfc.livestreamsales.application.model.stream.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamCreationResultDto
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamDto
import javax.inject.Inject

class StreamCreationResultDtoToMyStreamMapper @Inject constructor(
    private val streamDtoToMyStreamMapper: IEntityMapper<StreamDto, MyStream>
) : IEntityMapper<StreamCreationResultDto, MyStream> {
    override fun map(from: StreamCreationResultDto): MyStream? {
        return streamDtoToMyStreamMapper.map(from.data ?: return null)
    }
}