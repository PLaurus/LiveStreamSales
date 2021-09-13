package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.modules

import dagger.Module
import dagger.Provides
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.MinimalAudioBitrate
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.MinimalAudioSampleRate
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.MinimalVideoBitrate
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.MinimalVideoFps

@Module
class VideoStreamConstantsModule {
    @Provides
    @MinimalVideoBitrate
    internal fun provideMinimalVideoBitrate(): Int = 1024

    @Provides
    @MinimalVideoFps
    internal fun provideMinimalVideoFps(): Int = 30

    @Provides
    @MinimalAudioBitrate
    internal fun provideMinimalAudioBitrate(): Int = 32

    @Provides
    @MinimalAudioSampleRate
    internal fun provideMinimalAudioSampleRate(): Int = 44100
}