package tv.wfc.livestreamsales.application.model.stream.mapper

import tv.wfc.livestreamsales.application.base.entity.entitymapper.IEntityMapper
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamDto
import tv.wfc.livestreamsales.features.rest.api.stream.dto.StreamUpdateResultDto
import javax.inject.Inject

class StreamUpdateResultDtoToMyStreamMapper @Inject constructor(
    private val streamDtoToMyStreamMapper: IEntityMapper<StreamDto, MyStream>
) : IEntityMapper<StreamUpdateResultDto, MyStream> {
    override fun map(from: StreamUpdateResultDto): MyStream? {
        return streamDtoToMyStreamMapper.map(from.data ?: return null)
    }
}