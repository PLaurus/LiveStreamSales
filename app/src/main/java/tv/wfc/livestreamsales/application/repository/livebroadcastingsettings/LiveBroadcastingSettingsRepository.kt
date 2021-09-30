package tv.wfc.livestreamsales.application.repository.livebroadcastingsettings

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.datastore.qualifiers.LiveBroadcastingSettingsLocalDataStore
import tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.ILiveBroadcastingSettingsDataStore
import javax.inject.Inject

class LiveBroadcastingSettingsRepository @Inject constructor(
    @LiveBroadcastingSettingsLocalDataStore
    private val liveBroadcastingSettingsLocalDataStore: ILiveBroadcastingSettingsDataStore,
    @IoScheduler
    private val ioScheduler: Scheduler
): ILiveBroadcastingSettingsRepository {
    override fun getVideoResolutionPosition(): Single<Int> {
        return liveBroadcastingSettingsLocalDataStore
            .getVideoResolutionPosition()
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoResolutionPosition(position: Int): Completable {
        return liveBroadcastingSettingsLocalDataStore
            .updateVideoResolutionPosition(position)
            .subscribeOn(ioScheduler)
    }

    override fun getVideoBitrate(): Single<Int> {
        return liveBroadcastingSettingsLocalDataStore
            .getVideoBitrate()
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoBitrate(bitrate: Int): Completable {
        return liveBroadcastingSettingsLocalDataStore
            .updateVideoBitrate(bitrate)
            .subscribeOn(ioScheduler)
    }

    override fun getVideoFps(): Single<Int> {
        return liveBroadcastingSettingsLocalDataStore
            .getVideoFps()
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoFps(fps: Int): Completable {
        return liveBroadcastingSettingsLocalDataStore
            .updateVideoFps(fps)
            .subscribeOn(ioScheduler)
    }

    override fun getAudioBitrate(): Single<Int> {
        return liveBroadcastingSettingsLocalDataStore
            .getAudioBitrate()
            .subscribeOn(ioScheduler)
    }

    override fun updateAudioBitrate(bitrate: Int): Completable {
        return liveBroadcastingSettingsLocalDataStore
            .updateAudioBitrate(bitrate)
            .subscribeOn(ioScheduler)
    }

    override fun getAudioSampleRate(): Single<Int> {
        return liveBroadcastingSettingsLocalDataStore
            .getAudioSampleRate()
            .subscribeOn(ioScheduler)
    }

    override fun updateAudioSampleRate(sampleRate: Int): Completable {
        return liveBroadcastingSettingsLocalDataStore
            .updateAudioSampleRate(sampleRate)
            .subscribeOn(ioScheduler)
    }

    override fun getIsAudioChannelMono(): Single<Boolean> {
        return liveBroadcastingSettingsLocalDataStore
            .getIsAudioChannelMono()
            .subscribeOn(ioScheduler)
    }

    override fun updateIsAudioChannelMono(isMono: Boolean): Completable {
        return liveBroadcastingSettingsLocalDataStore
            .updateIsAudioChannelMono(isMono)
            .subscribeOn(ioScheduler)
    }

    override fun getIsEchoCancelerEnabled(): Single<Boolean> {
        return liveBroadcastingSettingsLocalDataStore
            .getIsEchoCancelerEnabled()
            .subscribeOn(ioScheduler)
    }

    override fun updateIsEchoCancelerEnabled(isEnabled: Boolean): Completable {
        return liveBroadcastingSettingsLocalDataStore
            .updateIsEchoCancelerEnabled(isEnabled)
            .subscribeOn(ioScheduler)
    }

    override fun getIsNoiseSuppressorEnabled(): Single<Boolean> {
        return liveBroadcastingSettingsLocalDataStore
            .getIsNoiseSuppressorEnabled()
            .subscribeOn(ioScheduler)
    }

    override fun updateIsNoiseSuppressorEnabled(isEnabled: Boolean): Completable {
        return liveBroadcastingSettingsLocalDataStore
            .updateIsNoiseSuppressorEnabled(isEnabled)
            .subscribeOn(ioScheduler)
    }
}