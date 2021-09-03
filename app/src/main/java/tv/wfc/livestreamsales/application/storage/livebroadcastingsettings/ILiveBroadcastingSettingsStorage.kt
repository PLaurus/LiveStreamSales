package tv.wfc.livestreamsales.application.storage.livebroadcastingsettings

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ILiveBroadcastingSettingsStorage {
    /**
     * @return Position of preferred video resolution at supported video resolutions list.
     */
    fun getVideoResolutionPosition(): Single<Int>

    /**
     * Updates preferred video resolution position at supported video resolutions list.
     */
    fun updateVideoResolutionPosition(position: Int): Completable

    /**
     * @return Preferred video bitrate for live broadcasting.
     */
    fun getVideoBitrate(): Single<Int>

    /**
     * Updates preferred video bitrate for live broadcasting.
     * @param bitrate Preferred video bitrate for live broadcasting.
     */
    fun updateVideoBitrate(bitrate: Int): Completable

    /**
     * @return Preferred video fps (frames per second) for live broadcasting.
     */
    fun getVideoFps(): Single<Int>

    /**
     * Updates preferred video fps (frames per second) for live broadcasting.
     * @param fps Preferred video frames per second for live broadcasting.
     */
    fun updateVideoFps(fps: Int): Completable

    /**
     * @return Preferred audio bitrate for live broadcasting.
     */
    fun getAudioBitrate(): Single<Int>

    /**
     * Updates preferred audio bitrate for live broadcasting.
     * @param bitrate Preferred audio bitrate for live broadcasting.
     */
    fun updateAudioBitrate(bitrate: Int): Completable

    /**
     * @return Preferred audio sample rate for live broadcasting.
     */
    fun getAudioSampleRate(): Single<Int>

    /**
     * Updates preferred audio sample rate for broadcast audio channel.
     * @param sampleRate Preferred sample rate for broadcast audio channel.
     */
    fun updateAudioSampleRate(sampleRate: Int): Completable

    /**
     * @return True if mono channel is preferred or false if stereo channel is preferred.
     */
    fun getIsAudioChannelMono(): Single<Boolean>

    /**
     * Sets either mono or stereo as preferred audio channel for live broadcasting.
     * @param isMono Is mono audio channel preferred. True means mono is preferred,
     * false means stereo is preferred.
     */
    fun updateIsAudioChannelMono(isMono: Boolean): Completable

    /**
     * @return Whether echo canceler is preferred to be on or off while live broadcasting.
     */
    fun getIsEchoCancelerEnabled(): Single<Boolean>

    /**
     * Updates echo canceler preferred state for live broadcasting.
     * @param isEnabled Whether echo canceler is preferred to be enabled while live broadcasting.
     */
    fun updateIsEchoCancelerEnabled(isEnabled: Boolean): Completable

    /**
     * @return Whether noise suppressor is preferred to be on or off while live broadcasting.
     */
    fun getIsNoiseSuppressorEnabled(): Single<Boolean>

    /**
     * Updates noise suppressor preferred state for live broadcasting.
     * @param isEnabled Whether noise suppressor is preferred to be enabled while live broadcasting.
     */
    fun updateIsNoiseSuppressorEnabled(isEnabled: Boolean): Completable
}