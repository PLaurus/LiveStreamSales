package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class StreamKeyError{
    object ValueIsRequired: StreamKeyError()
}