package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class ServerAddressError{
    object ValueIsRequired: ServerAddressError()
}