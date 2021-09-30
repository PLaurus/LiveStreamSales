package tv.wfc.livestreamsales.application.tools.serialization

import kotlin.reflect.KClass

interface ISerializationBehavior {
    fun <T: Any> serialize(instance: T, kClass: KClass<T>): String
    fun <T: Any> deserialize(data: String, kClass: KClass<T>): T?
}