package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.viewmodel

import androidx.lifecycle.LiveData
import com.laurus.p.tools.camera.model.Resolution
import com.laurus.p.tools.livedata.LiveEvent
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error.*
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.navigation.NextDestination

interface ILiveBroadcastingSettingsViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>
    val nextDestinationEvent: LiveEvent<NextDestination>

    val serverAddress: LiveData<String>
    val serverAddressError: LiveData<ServerAddressError?>
    val serverPort: LiveData<Int?>
    val serverPortError: LiveData<ServerPortError?>
    val streamKey: LiveData<String>
    val streamKeyError: LiveData<StreamKeyError?>
    val sourceUserName: LiveData<String>
    val sourceUserNameError: LiveData<SourceUserNameError?>
    val sourcePassword: LiveData<String>
    val sourcePasswordError: LiveData<SourcePasswordError?>
    val videoResolutions: LiveData<List<Resolution>>
    val videoResolutionPosition: LiveData<Int>
    val videoBitrate: LiveData<Int?>
    val videoBitrateError: LiveData<VideoBitrateError?>
    val videoFps: LiveData<Int?>
    val videoFpsError: LiveData<VideoFpsError?>
    val audioBitrate: LiveData<Int?>
    val audioBitrateError: LiveData<AudioBitrateError?>
    val audioSampleRate: LiveData<Int?>
    val audioSampleRateError: LiveData<AudioSampleRateError?>
    val isAudioChannelMono: LiveData<Boolean>
    val isEchoCancelerEnabled: LiveData<Boolean>
    val isNoiseSuppressorEnabled: LiveData<Boolean>
    val doSettingsHaveErrors: LiveData<Boolean>

    fun prepareData()
    fun refreshData()

    fun updateServerAddress(address: String?)
    fun updateServerPort(port: Int?)
    fun updateStreamKey(key: String?)
    fun updateSourceUserName(userName: String?)
    fun updateSourcePassword(password: String?)
    fun updateVideoResolutionPosition(position: Int)
    fun updateVideoBitrate(bitrate: Int?)
    fun updateVideoFps(fps: Int?)
    fun updateAudioBitrate(bitrate: Int?)
    fun updateAudioSampleRate(sampleRate: Int?)
    fun updateIsAudioChannelMono(isMono: Boolean)
    fun updateIsEchoCancelerEnabled(isEnabled: Boolean)
    fun updateIsNoiseSuppressorEnabled(isEnabled: Boolean)

    fun confirmSettings()

    fun intentToCloseCurrentDestination()
}