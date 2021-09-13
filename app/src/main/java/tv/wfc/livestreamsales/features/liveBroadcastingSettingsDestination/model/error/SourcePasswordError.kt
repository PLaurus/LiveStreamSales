package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class SourcePasswordError{
    object ValueIsRequired: SourcePasswordError()
}