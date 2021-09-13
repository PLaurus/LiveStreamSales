package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class ServerPortError{
    object ValueIsRequired: ServerPortError()
    object PortMustBeUnsignedInteger: ServerPortError()
}