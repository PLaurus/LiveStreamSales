package tv.wfc.livestreamsales.application.di.modules.datetime

import dagger.Module
import dagger.Provides
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import tv.wfc.livestreamsales.application.di.modules.datetime.qualifiers.RestDateTimeFormatter
import tv.wfc.livestreamsales.application.di.modules.datetime.qualifiers.RestDateTimePattern

@Module
class DateTimeModule {
    @Provides
    @RestDateTimePattern
    internal fun provideRestDateTimePattern() = "yyyy-MM-dd'T'HH:mm:ssZ"

    @Provides
    @RestDateTimeFormatter
    internal fun provideRestDateTimeFormatter(
        @RestDateTimePattern
        restDateTimePattern: String
    ): DateTimeFormatter = DateTimeFormat.forPattern(restDateTimePattern)
}