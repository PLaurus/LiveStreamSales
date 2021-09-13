package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error

sealed class VideoFpsError{
    object ValueIsRequired: VideoFpsError()
    data class FpsIsTooLow(val minimalFps: Int): VideoFpsError()
}