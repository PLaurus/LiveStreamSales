package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentLiveBroadcastingSettingsBinding
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.LiveBroadcastingSettingsComponent
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error.*
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.navigation.NextDestination
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.viewmodel.ILiveBroadcastingSettingsViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LiveBroadcastingSettingsFragment: BaseFragment(R.layout.fragment_live_broadcasting_settings) {
    private val navigationController by lazy { findNavController() }
    private var viewBinding: FragmentLiveBroadcastingSettingsBinding? = null
    private var viewShouldBeRefreshed = false
    private lateinit var liveBroadcastingSettingsComponent: LiveBroadcastingSettingsComponent

    @Inject
    lateinit var viewModel: ILiveBroadcastingSettingsViewModel

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeLiveBroadcastingSettingsComponent()
        injectDependencies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
        manageNavigation()
    }

    override fun onStart() {
        super.onStart()
        viewModel.prepareData()
        if(viewShouldBeRefreshed) viewShouldBeRefreshed = false
    }

    override fun onResume() {
        super.onResume()
        if(viewShouldBeRefreshed) viewModel.refreshData() else viewShouldBeRefreshed = true
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    private fun initializeLiveBroadcastingSettingsComponent() {
        if(::liveBroadcastingSettingsComponent.isInitialized) return

        liveBroadcastingSettingsComponent = appComponent
            .liveBroadcastingSettingsComponent()
            .create(this)
    }

    private fun injectDependencies() {
        liveBroadcastingSettingsComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentLiveBroadcastingSettingsBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeContentLoader() {
        viewBinding?.contentLoaderView?.apply{
            clearPreparationListeners()
            addOnDataIsPreparedListener(::onDataIsPrepared)
            attachViewModel(viewLifecycleOwner, viewModel)
            viewModel.isAnyOperationInProgress.observe(viewLifecycleOwner){ isAnyOperationInProgress ->
                when(isAnyOperationInProgress){
                    true -> showOperationProgress()
                    else -> hideOperationProgress()
                }
            }
        }
    }

    private fun manageNavigation() {
        viewModel.nextDestinationEvent.observe(viewLifecycleOwner) { nextDestination ->
            when(nextDestination) {
                NextDestination.Close -> navigationController.navigateUp()
                is NextDestination.LiveBroadcasting -> navigateToLiveBroadcasting(nextDestination)
            }
        }
    }

    private fun onDataIsPrepared(){
        initializeServerAddressTextInputLayout()
        initializeServerAddressEditText()
        initializeServerPortTextInputLayout()
        initializeServerPortEditText()
        initializeStreamKeyTextInputLayout()
        initializeStreamKeyEditText()
        initializeSourceUserNameTextInputLayout()
        initializeSourceUserNameEditText()
        initializeSourcePasswordTextInputLayout()
        initializeSourcePasswordEditText()
        initializeVideoResolutionAutoCompleteTextView()
        initializeVideoBitrateTextInputLayout()
        initializeVideoBitrateEditText()
        initializeVideoFpsTextInputLayout()
        initializeVideoFpsEditText()
        initializeAudioBitrateTextInputLayout()
        initializeAudioBitrateEditText()
        initializeAudioSampleRateTextInputLayout()
        initializeAudioSampleRateEditText()
        initializeAudioChannelAutoCompleteTextView()
        initializeAudioEchoCancelerSwitchMaterial()
        initializeAudioNoiseSuppressorSwitchMaterial()
        initializeConfirmButton()
    }

    private fun navigateToLiveBroadcasting(liveBroadcastingDestination: NextDestination.LiveBroadcasting) {
        liveBroadcastingDestination.run {
            val action = LiveBroadcastingSettingsFragmentDirections.toLiveBroadcastingDestination(
                serverAddress,
                serverPort,
                streamKey,
                sourceUserName,
                sourcePassword
            )
            navigationController.navigate(action)
        }
    }

    private fun initializeServerAddressTextInputLayout() {
        viewModel.serverAddressError.observe(viewLifecycleOwner) { serverAddressError ->
            val errorText = when(serverAddressError) {
                ServerAddressError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_server_address_error_field_is_required)
                else -> ""
            }

            viewBinding?.serverAddressTextInputLayout?.apply {
                isErrorEnabled = serverAddressError != null
                error = errorText
            }
        }
    }

    private fun initializeServerAddressEditText() {
        viewBinding?.serverAddressEditText?.run {
            viewModel.serverAddress.observe(viewLifecycleOwner, Observer{ serverAddress ->
                val currentText = text?.toString() ?: ""
                if(currentText == serverAddress) return@Observer
                setText(serverAddress, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateServerAddress(editable.toString())
            }
        }
    }

    private fun initializeServerPortTextInputLayout() {
        viewModel.serverPortError.observe(viewLifecycleOwner) { serverPortError ->
            val errorText = when(serverPortError) {
                ServerPortError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_server_port_error_field_is_required)
                ServerPortError.PortMustBeUnsignedInteger -> getString(R.string.fragment_live_broadcasting_settings_server_port_error_must_be_unsigned_integer)
                else -> ""
            }

            viewBinding?.serverPortTextInputLayout?.apply {
                isErrorEnabled = serverPortError != null
                error = errorText
            }
        }
    }

    private fun initializeServerPortEditText() {
        viewBinding?.serverPortEditText?.run {
            viewModel.serverPort.observe(viewLifecycleOwner, Observer{ serverPort ->
                val currentText = text?.toString() ?: ""
                val newText = serverPort?.toString() ?: ""
                if(currentText == newText) return@Observer
                setText(newText, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                val newPort = editable?.toString()?.toIntOrNull()
                viewModel.updateServerPort(newPort)
            }
        }
    }

    private fun initializeStreamKeyTextInputLayout() {
        viewModel.streamKeyError.observe(viewLifecycleOwner) { streamKeyError ->
            val errorText = when(streamKeyError) {
                StreamKeyError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_stream_key_error_field_is_required)
                else -> ""
            }

            viewBinding?.streamKeyTextInputLayout?.run{
                isErrorEnabled = streamKeyError != null
                error = errorText
            }
        }
    }

    private fun initializeStreamKeyEditText() {
        viewBinding?.streamKeyEditText?.run {
            viewModel.streamKey.observe(viewLifecycleOwner, Observer { streamKey ->
                val currentText = text?.toString() ?: ""
                if(currentText == streamKey) return@Observer
                setText(streamKey, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                val newStreamKey = editable?.toString() ?: ""
                viewModel.updateStreamKey(newStreamKey)
            }
        }
    }

    private fun initializeSourceUserNameTextInputLayout() {
        viewModel.sourceUserNameError.observe(viewLifecycleOwner) { sourceUserNameError ->
            val errorText = when(sourceUserNameError) {
                SourceUserNameError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_source_user_name_error_field_is_required)
                else -> ""
            }

            viewBinding?.sourceUserNameTextInputLayout?.apply{
                isErrorEnabled = sourceUserNameError != null
                error = errorText
            }
        }
    }

    private fun initializeSourceUserNameEditText() {
        viewBinding?.sourceUserNameEditText?.run {
            viewModel.sourceUserName.observe(viewLifecycleOwner, Observer { sourceUserName ->
                val currentText = text?.toString() ?: ""
                if(currentText == sourceUserName) return@Observer
                setText(sourceUserName, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                val newSourceUserName = editable?.toString() ?: ""
                viewModel.updateSourceUserName(newSourceUserName)
            }
        }
    }

    private fun initializeSourcePasswordTextInputLayout() {
        viewModel.sourcePasswordError.observe(viewLifecycleOwner) { sourcePasswordError ->
            val errorText = when(sourcePasswordError) {
                SourcePasswordError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_source_password_error_field_is_required)
                else -> ""
            }

            viewBinding?.sourcePasswordTextInputLayout?.apply {
                isErrorEnabled = sourcePasswordError != null
                error = errorText
            }
        }
    }

    private fun initializeSourcePasswordEditText() {
        viewBinding?.sourcePasswordEditText?.run {
            viewModel.sourcePassword.observe(viewLifecycleOwner, Observer { sourcePassword ->
                val currentText = text?.toString() ?: ""
                if(currentText == sourcePassword) return@Observer
                setText(sourcePassword, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener{ editable ->
                val newPassword = editable?.toString() ?: ""
                viewModel.updateSourcePassword(newPassword)
            }
        }
    }

    private fun initializeVideoResolutionAutoCompleteTextView() {
        viewBinding?.videoResolutionAutoCompleteTextView?.run {
            val resolutionsAdapter = ArrayAdapter(context, R.layout.list_item_exposed_dropdown_menu_item, mutableListOf<String>())
            setAdapter(resolutionsAdapter)

            viewModel.videoResolutions.observe(viewLifecycleOwner){ videoResolutions ->
                val newVideoResolutions = mutableListOf<String>().apply{
                    videoResolutions.forEach { add("${it.width} X ${it.height}") }
                }

                resolutionsAdapter.run{
                    clear()
                    addAll(newVideoResolutions)
                }
            }

            viewModel.videoResolutionPosition.observe(viewLifecycleOwner) { videoResolutionPosition ->
                val selectedVideoResolutionText = resolutionsAdapter.getItem(videoResolutionPosition) ?: ""
                setText(selectedVideoResolutionText, false)
            }

            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    viewModel.updateVideoResolutionPosition(position)
                }
        }
    }

    private fun initializeVideoBitrateTextInputLayout() {
        viewModel.videoBitrateError.observe(viewLifecycleOwner) { videoBitrateError ->
            val errorText = when(videoBitrateError) {
                VideoBitrateError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_video_bitrate_error_field_is_required)
                is VideoBitrateError.BitrateIsTooLow -> getString(R.string.fragment_live_broadcasting_settings_video_bitrate_error_too_low, videoBitrateError.minimalBitrate)
                else -> ""
            }

            viewBinding?.videoBitrateTextInputLayout?.apply {
                isErrorEnabled = videoBitrateError != null
                error = errorText
            }
        }
    }

    private fun initializeVideoBitrateEditText() {
        viewBinding?.videoBitrateEditText?.run {
            viewModel.videoBitrate.observe(viewLifecycleOwner, Observer { videoBitrate ->
                val currentText = text?.toString() ?: ""
                val newText = videoBitrate?.toString() ?: ""
                if(currentText == newText) return@Observer
                setText(newText, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener{ editable ->
                val newVideoBitrate = editable?.toString()?.toIntOrNull()
                viewModel.updateVideoBitrate(newVideoBitrate)
            }
        }
    }

    private fun initializeVideoFpsTextInputLayout() {
        viewModel.videoFpsError.observe(viewLifecycleOwner) { videoFpsError ->
            val errorText = when(videoFpsError) {
                VideoFpsError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_fps_error_field_is_required)
                is VideoFpsError.FpsIsTooLow -> getString(R.string.fragment_live_broadcasting_settings_fps_error_too_low, videoFpsError.minimalFps)
                else -> ""
            }

            viewBinding?.videoFpsTextInputLayout?.apply {
                isErrorEnabled = videoFpsError != null
                error = errorText
            }
        }
    }

    private fun initializeVideoFpsEditText() {
        viewBinding?.videoFpsEditText?.run {
            viewModel.videoFps.observe(viewLifecycleOwner, Observer { videoFps ->
                val currentText = text?.toString() ?: ""
                val newText = videoFps?.toString() ?: ""
                if(currentText == newText) return@Observer
                setText(newText, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener{ editable ->
                val newVideoFps = editable?.toString()?.toIntOrNull()
                viewModel.updateVideoFps(newVideoFps)
            }
        }
    }

    private fun initializeAudioBitrateTextInputLayout() {
        viewModel.audioBitrateError.observe(viewLifecycleOwner) { audioBitrateError ->
            val errorText = when(audioBitrateError) {
                AudioBitrateError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_audio_bitrate_error_field_is_required)
                is AudioBitrateError.BitrateIsTooLow -> getString(R.string.fragment_live_broadcasting_settings_audio_bitrate_error_too_low, audioBitrateError.minimalBitrate)
                else -> ""
            }

            viewBinding?.audioBitrateTextInputLayout?.apply {
                isErrorEnabled = audioBitrateError != null
                error = errorText
            }
        }
    }

    private fun initializeAudioBitrateEditText() {
        viewBinding?.audioBitrateEditText?.run {
            viewModel.audioBitrate.observe(viewLifecycleOwner, Observer { audioBitrate ->
                val currentText = text?.toString() ?: ""
                val newText = audioBitrate?.toString() ?: ""
                if(currentText == newText) return@Observer
                setText(newText, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener{ editable ->
                val newAudioBitrate = editable?.toString()?.toIntOrNull()
                viewModel.updateAudioBitrate(newAudioBitrate)
            }
        }
    }

    private fun initializeAudioSampleRateTextInputLayout() {
        viewModel.audioSampleRateError.observe(viewLifecycleOwner) { audioSampleRateError ->
            val errorText = when(audioSampleRateError) {
                AudioSampleRateError.ValueIsRequired -> getString(R.string.fragment_live_broadcasting_settings_audio_sample_rate_error_field_is_required)
                is AudioSampleRateError.SampleRateIsTooLow -> getString(R.string.fragment_live_broadcasting_settings_audio_sample_rate_error_too_low, audioSampleRateError.minimalSampleRate)
                else -> ""
            }

            viewBinding?.audioSampleRateTextInputLayout?.apply {
                isErrorEnabled = audioSampleRateError != null
                error = errorText
            }
        }
    }

    private fun initializeAudioSampleRateEditText() {
        viewBinding?.audioSampleRateEditText?.run {
            viewModel.audioSampleRate.observe(viewLifecycleOwner, Observer { audioSampleRate ->
                val currentText = text?.toString() ?: ""
                val newText = audioSampleRate?.toString() ?: ""
                if(currentText == newText) return@Observer
                setText(newText, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener{ editable ->
                val newAudioSampleRate = editable?.toString()?.toIntOrNull()
                viewModel.updateAudioSampleRate(newAudioSampleRate)
            }
        }
    }

    private fun initializeAudioChannelAutoCompleteTextView() {
        viewBinding?.audioChannelAutoCompleteTextView?.run {
            val audioChannelAdapter = ArrayAdapter(context, R.layout.list_item_exposed_dropdown_menu_item, arrayListOf(
                getString(R.string.fragment_live_broadcasting_settings_audio_channel_mono),
                getString(R.string.fragment_live_broadcasting_settings_audio_channel_stereo)
            ))
            setAdapter(audioChannelAdapter)

            viewModel.isAudioChannelMono.observe(viewLifecycleOwner) { isAudioChannelMono ->
                val selectedChannelText = when(isAudioChannelMono) {
                    true -> audioChannelAdapter.getItem(0)
                    else -> audioChannelAdapter.getItem(1)
                }

                setText(selectedChannelText, false)
            }

            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val isMono = when(position){
                        0 -> true
                        else -> false
                    }
                    viewModel.updateIsAudioChannelMono(isMono)
                }
        }
    }

    private fun initializeAudioEchoCancelerSwitchMaterial() {
        viewBinding?.audioEchoCancelerSwitchMaterial?.run {
            viewModel.isEchoCancelerEnabled.observe(viewLifecycleOwner, Observer { isEchoCancelerEnabled ->
                if(isChecked == isEchoCancelerEnabled) return@Observer
                isChecked = isEchoCancelerEnabled
            })

            setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateIsEchoCancelerEnabled(isChecked)
            }
        }
    }

    private fun initializeAudioNoiseSuppressorSwitchMaterial() {
        viewBinding?.audioNoiseSuppressorSwitchMaterial?.run {
            viewModel.isNoiseSuppressorEnabled.observe(viewLifecycleOwner, Observer { isNoiseSuppressorEnabled ->
                if(isChecked == isNoiseSuppressorEnabled) return@Observer
                isChecked = isNoiseSuppressorEnabled
            })

            setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateIsNoiseSuppressorEnabled(isChecked)
            }
        }
    }

    private fun initializeConfirmButton() {
        viewBinding?.confirmButton?.run {
            viewModel.doSettingsHaveErrors.observe(viewLifecycleOwner) { doSettingsHaveErrors ->
                isEnabled = !doSettingsHaveErrors
            }

            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.confirmSettings() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }
}