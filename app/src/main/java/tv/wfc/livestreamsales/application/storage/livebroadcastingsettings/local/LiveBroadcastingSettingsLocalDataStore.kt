package tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.local

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.sharedpreferences.qualifiers.LiveBroadcastingSharedPreferences
import tv.wfc.livestreamsales.application.storage.livebroadcastingsettings.ILiveBroadcastingSettingsDataStore
import javax.inject.Inject

class LiveBroadcastingSettingsLocalDataStore @Inject constructor(
    @LiveBroadcastingSharedPreferences
    private val liveBroadcastingSharedPreferences: SharedPreferences,
    @IoScheduler
    private val ioScheduler: Scheduler
): ILiveBroadcastingSettingsDataStore {
    companion object{
        private const val VIDEO_RESOLUTION_LIST_POSITION_KEY = "video_resolution_list_position"
        private const val VIDEO_BITRATE_KEY = "video_bitrate"
        private const val VIDEO_FPS_KEY = "video_fps"
        private const val AUDIO_BITRATE_KEY = "audio_bitrate"
        private const val AUDIO_SAMPLE_RATE_KEY = "audio_sample_rate"
        private const val IS_AUDIO_CHANNEL_MONO_KEY = "is_audio_channel_mono"
        private const val IS_ECHO_CANCELER_ENABLED_KEY = "is_echo_canceler_enabled"
        private const val IS_NOISE_SUPPRESSOR_ENABLED_KEY = "is_noise_suppressor_enabled"
        private const val DEFAULT_VIDEO_RESOLUTION_LIST_POSITION = 0
        private const val DEFAULT_VIDEO_BITRATE = 2500
        private const val DEFAULT_VIDEO_FPS = 30
        private const val DEFAULT_AUDIO_BITRATE = 128
        private const val DEFAULT_AUDIO_SAMPLE_RATE = 44100
        private const val DEFAULT_IS_AUDIO_CHANNEL_MONO = false
        private const val DEFAULT_IS_ECHO_CANCELER_ENABLED = false
        private const val DEFAULT_IS_NOISE_SUPPRESSOR_ENABLED = false
    }

    override fun getVideoResolutionPosition(): Single<Int> {
        return Single
            .fromCallable {
                liveBroadcastingSharedPreferences.getInt(VIDEO_RESOLUTION_LIST_POSITION_KEY, DEFAULT_VIDEO_RESOLUTION_LIST_POSITION)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoResolutionPosition(position: Int): Completable {
        return Completable
            .fromRunnable {
                liveBroadcastingSharedPreferences.edit(commit = true){
                    putInt(VIDEO_RESOLUTION_LIST_POSITION_KEY, position)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getVideoBitrate(): Single<Int> {
        return Single
            .fromCallable{
                liveBroadcastingSharedPreferences.getInt(VIDEO_BITRATE_KEY, DEFAULT_VIDEO_BITRATE)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoBitrate(bitrate: Int): Completable {
        return Completable
            .fromRunnable {
                liveBroadcastingSharedPreferences.edit(commit = true){
                    putInt(VIDEO_BITRATE_KEY, bitrate)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getVideoFps(): Single<Int> {
        return Single
            .fromCallable {
                liveBroadcastingSharedPreferences.getInt(VIDEO_FPS_KEY, DEFAULT_VIDEO_FPS)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateVideoFps(fps: Int): Completable {
        return Completable
            .fromRunnable{
                liveBroadcastingSharedPreferences.edit(commit = true){
                    putInt(VIDEO_FPS_KEY, fps)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getAudioBitrate(): Single<Int> {
        return Single
            .fromCallable{
                liveBroadcastingSharedPreferences.getInt(AUDIO_BITRATE_KEY, DEFAULT_AUDIO_BITRATE)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateAudioBitrate(bitrate: Int): Completable {
        return Completable
            .fromRunnable {
                liveBroadcastingSharedPreferences.edit(commit = true){
                    putInt(AUDIO_BITRATE_KEY, bitrate)
                }
            }
    }

    override fun getAudioSampleRate(): Single<Int> {
        return Single
            .fromCallable {
                liveBroadcastingSharedPreferences.getInt(AUDIO_SAMPLE_RATE_KEY, DEFAULT_AUDIO_SAMPLE_RATE)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateAudioSampleRate(sampleRate: Int): Completable {
        return Completable
            .fromRunnable {
                liveBroadcastingSharedPreferences.edit(commit = true){
                    putInt(AUDIO_SAMPLE_RATE_KEY, sampleRate)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getIsAudioChannelMono(): Single<Boolean> {
        return Single
            .fromCallable {
                liveBroadcastingSharedPreferences.getBoolean(IS_AUDIO_CHANNEL_MONO_KEY, DEFAULT_IS_AUDIO_CHANNEL_MONO)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateIsAudioChannelMono(isMono: Boolean): Completable {
        return Completable
            .fromRunnable{
                liveBroadcastingSharedPreferences.edit(commit = true){
                    putBoolean(IS_AUDIO_CHANNEL_MONO_KEY, isMono)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getIsEchoCancelerEnabled(): Single<Boolean> {
        return Single
            .fromCallable {
                liveBroadcastingSharedPreferences.getBoolean(IS_ECHO_CANCELER_ENABLED_KEY, DEFAULT_IS_ECHO_CANCELER_ENABLED)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateIsEchoCancelerEnabled(isEnabled: Boolean): Completable {
        return Completable
            .fromRunnable{
                liveBroadcastingSharedPreferences.edit(commit = true){
                    putBoolean(IS_ECHO_CANCELER_ENABLED_KEY, isEnabled)
                }
            }
            .subscribeOn(ioScheduler)
    }

    override fun getIsNoiseSuppressorEnabled(): Single<Boolean> {
        return Single
            .fromCallable{
                liveBroadcastingSharedPreferences.getBoolean(IS_NOISE_SUPPRESSOR_ENABLED_KEY, DEFAULT_IS_NOISE_SUPPRESSOR_ENABLED)
            }
            .subscribeOn(ioScheduler)
    }

    override fun updateIsNoiseSuppressorEnabled(isEnabled: Boolean): Completable {
        return Completable
            .fromRunnable{
                liveBroadcastingSharedPreferences.edit(commit = true){
                    putBoolean(IS_NOISE_SUPPRESSOR_ENABLED_KEY, isEnabled)
                }
            }
            .subscribeOn(ioScheduler)
    }
}