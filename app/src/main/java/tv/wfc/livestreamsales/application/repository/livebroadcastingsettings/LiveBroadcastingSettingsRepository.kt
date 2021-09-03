package tv.wfc.livestreamsales.application.repository.livebroadcastingsettings

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.storage.qualifiers.LiveBroadcastingSettingsLocalStorage
import tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.ILiveBroadcastingSettingsStorage
import javax.inject.Inject

class LiveBroadcastingSettingsRepository @Inject constructor(
    @LiveBroadcastingSettingsLocalStorage
    private val liveBroadcastingSettingsStorage: ILiveBroadcastingSettingsStorage,
    @IoScheduler
    private val ioScheduler: Scheduler
): ILiveBroadcastingSettingsRepository {
    override fun getVideoResolutionPosition(): Single<Int> {
        return liveBroadcastingSettingsStorage
            .getVideoResolutionPosition()
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoResolutionPosition(position: Int): Completable {
        return liveBroadcastingSettingsStorage
            .updateVideoResolutionPosition(position)
            .subscribeOn(ioScheduler)
    }

    override fun getVideoBitrate(): Single<Int> {
        return liveBroadcastingSettingsStorage
            .getVideoBitrate()
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoBitrate(bitrate: Int): Completable {
        return liveBroadcastingSettingsStorage
            .updateVideoBitrate(bitrate)
            .subscribeOn(ioScheduler)
    }

    override fun getVideoFps(): Single<Int> {
        return liveBroadcastingSettingsStorage
            .getVideoFps()
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoFps(fps: Int): Completable {
        return liveBroadcastingSettingsStorage
            .updateVideoFps(fps)
            .subscribeOn(ioScheduler)
    }

    override fun getAudioBitrate(): Single<Int> {
        return liveBroadcastingSettingsStorage
            .getAudioBitrate()
            .subscribeOn(ioScheduler)
    }

    override fun updateAudioBitrate(bitrate: Int): Completable {
        return liveBroadcastingSettingsStorage
            .updateAudioBitrate(bitrate)
            .subscribeOn(ioScheduler)
    }

    override fun getAudioSampleRate(): Single<Int> {
        return liveBroadcastingSettingsStorage
            .getAudioSampleRate()
            .subscribeOn(ioScheduler)
    }

    override fun updateAudioSampleRate(sampleRate: Int): Completable {
        return liveBroadcastingSettingsStorage
            .updateAudioSampleRate(sampleRate)
            .subscribeOn(ioScheduler)
    }

    override fun getIsAudioChannelMono(): Single<Boolean> {
        return liveBroadcastingSettingsStorage
            .getIsAudioChannelMono()
            .subscribeOn(ioScheduler)
    }

    override fun updateIsAudioChannelMono(isMono: Boolean): Completable {
        return liveBroadcastingSettingsStorage
            .updateIsAudioChannelMono(isMono)
            .subscribeOn(ioScheduler)
    }

    override fun getIsEchoCancelerEnabled(): Single<Boolean> {
        return liveBroadcastingSettingsStorage
            .getIsEchoCancelerEnabled()
            .subscribeOn(ioScheduler)
    }

    override fun updateIsEchoCancelerEnabled(isEnabled: Boolean): Completable {
        return liveBroadcastingSettingsStorage
            .updateIsEchoCancelerEnabled(isEnabled)
            .subscribeOn(ioScheduler)
    }

    override fun getIsNoiseSuppressorEnabled(): Single<Boolean> {
        return liveBroadcastingSettingsStorage
            .getIsNoiseSuppressorEnabled()
            .subscribeOn(ioScheduler)
    }

    override fun updateIsNoiseSuppressorEnabled(isEnabled: Boolean): Completable {
        return liveBroadcastingSettingsStorage
            .updateIsNoiseSuppressorEnabled(isEnabled)
            .subscribeOn(ioScheduler)
    }
}