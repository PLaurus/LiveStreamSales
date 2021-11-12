package tv.wfc.livestreamsales.application.model.datetime.mapper

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.di.modules.datetime.qualifiers.RestDateTimeFormatter
import javax.inject.Inject

class JodaDateTimeToIso8601StringMapper @Inject constructor(
    @RestDateTimeFormatter
    private val restDateTimeFormatter: DateTimeFormatter
) : IEntityMapper<DateTime, String> {
    override fun map(from: DateTime): String? = from.toString(restDateTimeFormatter)
}