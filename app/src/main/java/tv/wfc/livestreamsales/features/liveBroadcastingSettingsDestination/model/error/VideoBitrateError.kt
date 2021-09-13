package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class VideoBitrateError{
    object ValueIsRequired: VideoBitrateError()
    data class BitrateIsTooLow(val minimalBitrate: Int): VideoBitrateError()
}