package tv.wfc.livestreamsales.application.model.products.specification

import androidx.annotation.ColorInt

sealed class Specification<out T>(val name: String, val value: T) {
    class ColorSpecification(name: String, @ColorInt value: Int): Specification<Int>(name, value)
    class DescriptiveSpecification(name: String, value: String): Specification<String>(name, value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Specification<*>) return false
        if (javaClass != other.javaClass) return false

        if (name != other.name) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}