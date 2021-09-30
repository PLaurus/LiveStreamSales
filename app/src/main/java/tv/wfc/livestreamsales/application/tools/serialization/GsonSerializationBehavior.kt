package tv.wfc.livestreamsales.application.tools.serialization

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import javax.inject.Inject
import kotlin.reflect.KClass

class GsonSerializationBehavior @Inject constructor(
    private val gson: Gson,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ISerializationBehavior {
    override fun <T : Any> serialize(instance: T, kClass: KClass<T>): String {
        return gson.toJson(instance, kClass.java)
    }

    override fun <T : Any> deserialize(data: String, kClass: KClass<T>): T? {
        return try{
            gson.fromJson(data, kClass.java)
        } catch (jsonSyntaxException: JsonSyntaxException) {
            applicationErrorsLogger.logError(jsonSyntaxException)
            null
        }
    }
}