package tv.wfc.livestreamsales.application.model.stream.mapper

import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamDto
import tv.wfc.livestreamsales.features.rest.api.mystreams.dto.MyStreamsDto
import javax.inject.Inject

class MyStreamsDtoToListOfMyStreamsMapper @Inject constructor(
    private val myStreamDtoToMyStreamMapper: IEntityMapper<MyStreamDto, MyStream>
) : IEntityMapper<MyStreamsDto, @JvmSuppressWildcards List<MyStream>> {
    override fun map(from: MyStreamsDto): List<MyStream>? {
        return from.data
            ?.filterNotNull()
            ?.mapNotNull { myStreamDtoToMyStreamMapper.map(it) }
    }
}