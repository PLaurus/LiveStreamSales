package tv.wfc.livestreamsales.features.liveBroadcastingDestination.viewmodel

import androidx.lifecycle.LiveData
import com.laurus.p.tools.livedata.LiveEvent
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.model.navigation.NextDestination

interface ILiveBroadcastingViewModel: INeedPreparationViewModel {
    val isAnyOperationInProgress: LiveData<Boolean>
    val nextDestinationEvent: LiveEvent<NextDestination>

    val endPointAddress: LiveData<String?>
    val sourceUserName: LiveData<String?>
    val sourcePassword: LiveData<String?>

    val preferredVideoResolutionPosition: LiveData<Int?>
    val preferredVideoBitrate: LiveData<Int?>
    val preferredVideoFps: LiveData<Int?>
    val preferredAudioBitrate: LiveData<Int?>
    val preferredAudioSampleRate: LiveData<Int?>
    val preferredIsAudioChannelMono: LiveData<Boolean?>
    val preferredIsEchoCancelerEnabled: LiveData<Boolean?>
    val preferredIsNoiseSuppressorEnabled: LiveData<Boolean?>

    fun prepareData(
        serverAddress: String,
        serverPort: Int,
        streamKey: String,
        sourceUserName: String,
        sourcePassword: String
    )
}