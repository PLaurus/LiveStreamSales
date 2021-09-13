package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class SourceUserNameError{
    object ValueIsRequired: SourceUserNameError()
}