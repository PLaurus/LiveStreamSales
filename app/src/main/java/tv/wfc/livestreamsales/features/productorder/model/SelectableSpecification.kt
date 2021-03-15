package tv.wfc.livestreamsales.features.productorder.model

sealed class SelectableSpecification<T>(
    val name: String,
    val availableValues: Set<T>,
    selectedValue: T? = null
) {
    var selectedValue: T? = selectedValue
        private set

    class ColorSpecification(
        name: String,
        availableValues: Set<Int>,
        selectedValue: Int? = null
    ) : SelectableSpecification<Int>(name, availableValues, selectedValue)

    class DescriptiveSpecification(
        name: String,
        availableValues: Set<String>,
        selectedValue: String? = null
    ) : SelectableSpecification<String>(name, availableValues, selectedValue)

    fun selectValue(position: Int){
        selectedValue = availableValues.elementAtOrNull(position)
    }

    fun deselectValue(){
        selectedValue = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SelectableSpecification<*>) return false
        if (javaClass != other.javaClass) return false

        if (name != other.name) return false
        if (availableValues != other.availableValues) return false
        if (selectedValue != other.selectedValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + availableValues.hashCode()
        result = 31 * result + (selectedValue?.hashCode() ?: 0)
        return result
    }
}