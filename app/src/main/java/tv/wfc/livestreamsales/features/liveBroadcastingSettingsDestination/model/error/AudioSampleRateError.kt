package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class AudioSampleRateError{
    object ValueIsRequired: AudioSampleRateError()
    data class SampleRateIsTooLow(val minimalSampleRate: Int): AudioSampleRateError()
}