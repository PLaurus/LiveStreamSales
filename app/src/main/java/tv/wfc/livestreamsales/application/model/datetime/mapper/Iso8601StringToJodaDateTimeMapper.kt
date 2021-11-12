package tv.wfc.livestreamsales.application.model.datetime.mapper

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import tv.wfc.core.entity.IEntityMapper
import tv.wfc.livestreamsales.application.di.modules.datetime.qualifiers.RestDateTimeFormatter
import javax.inject.Inject

class Iso8601StringToJodaDateTimeMapper @Inject constructor(
    @RestDateTimeFormatter
    private val restDateTimeFormatter: DateTimeFormatter
) : IEntityMapper<String, DateTime> {
    override fun map(from: String): DateTime? = try {
        restDateTimeFormatter.parseDateTime(from)
    } catch (exception: Exception) {
        null
    }
}