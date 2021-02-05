package tv.wfc.livestreamsales.application.tools.gson.typeadapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class DateTimeTypeAdapter @Inject constructor(
    private val dateTimeFormatter: DateTimeFormatter
): TypeAdapter<DateTime>() {
    override fun write(jsonWriter: JsonWriter?, dateTime: DateTime?) {
        if(dateTime == null){
            jsonWriter?.nullValue()
        } else{
            jsonWriter?.value(dateTime.toString(dateTimeFormatter))
        }
    }

    override fun read(jsonReader: JsonReader?): DateTime? {
        return if (jsonReader?.peek() == JsonToken.NULL) {
            jsonReader.nextNull()
            null
        } else {
            DateTime.parse(jsonReader?.nextString(), dateTimeFormatter)
        }
    }
}