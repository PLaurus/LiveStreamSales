package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class AudioBitrateError{
    object ValueIsRequired: AudioBitrateError()
    data class BitrateIsTooLow(val minimalBitrate: Int): AudioBitrateError()
}