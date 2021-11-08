package tv.wfc.livestreamsales.features.streamcreation.model

sealed class NextDestination {
    object Close: NextDestination()

    data class StartDatePicker(val minDate: Long, val maxDate: Long? = null): NextDestination()

    object StartTimePicker: NextDestination()

    data class EndDatePicker(val minDate: Long): NextDestination()

    object EndTimePicker: NextDestination()

    object PreviewImagePicker: NextDestination()
}