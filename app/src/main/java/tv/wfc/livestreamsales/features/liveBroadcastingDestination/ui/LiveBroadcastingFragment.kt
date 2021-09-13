package tv.wfc.livestreamsales.features.liveBroadcastingDestination.ui

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding4.view.clicks
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.encoder.input.video.CameraOpenException
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentLiveBroadcastingBinding
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.LiveBroadcastingComponent
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.model.navigation.NextDestination
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.viewmodel.ILiveBroadcastingViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LiveBroadcastingFragment: BaseFragment(R.layout.fragment_live_broadcasting) {
    private val navigationController by lazy { findNavController() }
    private val navigationArguments by navArgs<LiveBroadcastingFragmentArgs>()
    private val rtmpConnectCheckers = mutableListOf<ConnectCheckerRtmp>()
    private var viewBinding: FragmentLiveBroadcastingBinding? = null
    private var rtmpCamera1: RtmpCamera1? = null
    private lateinit var liveBroadcastingComponent: LiveBroadcastingComponent

    @Inject
    lateinit var viewModel: ILiveBroadcastingViewModel

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
        initializeLiveBroadcastingComponent()
        injectDependencies()
        viewModel.prepareData(
            navigationArguments.serverAddress,
            navigationArguments.serverPort,
            navigationArguments.streamKey,
            navigationArguments.sourceUserName,
            navigationArguments.sourcePassword
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
        manageNavigation()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onPause()
    }

    override fun onDestroyView() {
        rtmpConnectCheckers.clear()
        unbindView()
        super.onDestroyView()
    }

    private fun initializeLiveBroadcastingComponent() {
        if(::liveBroadcastingComponent.isInitialized) return
        liveBroadcastingComponent = appComponent
            .liveBroadcastingComponent()
            .create(this)
    }

    private fun injectDependencies() {
        liveBroadcastingComponent.inject(this)
    }

    private fun bindView(view: View) {
        viewBinding = FragmentLiveBroadcastingBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeContentLoader() {
        viewBinding?.contentLoaderView?.run{
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
            }
        }
    }

    private fun onDataIsPrepared() {
        initializeRtmpCamera1()
        initializeSurfaceView()
        initializeToast()
        initializeSwitchBroadcastStateButton()
        initializeCurrentVideoBitrate()
        initializeSwitchMicrophoneOnOffButton()
        initializeSwitchCameraFacingButton()
        initializeSwitchCameraOnOffButton()
    }

    private fun initializeRtmpCamera1() {
        viewBinding?.run{
            rtmpConnectCheckers.add(object: ConnectCheckerRtmp {
                override fun onAuthErrorRtmp() = Unit

                override fun onAuthSuccessRtmp() = Unit

                override fun onConnectionFailedRtmp(reason: String) {
                    Completable
                        .fromRunnable { rtmpCamera1?.stopStream() }
                        .subscribeOn(mainThreadScheduler)
                        .observeOn(mainThreadScheduler)
                        .subscribeBy(applicationErrorsLogger::logError)
                        .addTo(viewScopeDisposables)
                }

                override fun onConnectionStartedRtmp(rtmpUrl: String) = Unit

                override fun onConnectionSuccessRtmp() = Unit

                override fun onDisconnectRtmp() = Unit

                override fun onNewBitrateRtmp(bitrate: Long) = Unit
            })

            rtmpCamera1 = RtmpCamera1(surfaceView, object: ConnectCheckerRtmp {
                override fun onAuthErrorRtmp() {
                    rtmpConnectCheckers.forEach { it.onAuthErrorRtmp() }
                }

                override fun onAuthSuccessRtmp() {
                    rtmpConnectCheckers.forEach { it.onAuthSuccessRtmp() }
                }

                override fun onConnectionFailedRtmp(reason: String) {
                    rtmpConnectCheckers.forEach { it.onConnectionFailedRtmp(reason) }
                }

                override fun onConnectionStartedRtmp(rtmpUrl: String) {
                    rtmpConnectCheckers.forEach { it.onConnectionStartedRtmp(rtmpUrl) }
                }

                override fun onConnectionSuccessRtmp() {
                    rtmpConnectCheckers.forEach { it.onConnectionSuccessRtmp() }
                }

                override fun onDisconnectRtmp() {
                    rtmpConnectCheckers.forEach { it.onDisconnectRtmp() }
                }

                override fun onNewBitrateRtmp(bitrate: Long) {
                    rtmpConnectCheckers.forEach { it.onNewBitrateRtmp(bitrate) }
                }
            })
        }
    }

    private fun initializeSurfaceView() {
        viewBinding?.surfaceView?.run {
            holder.addCallback(object: SurfaceHolder.Callback{
                override fun surfaceCreated(holder: SurfaceHolder) = Unit

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ){
                    rtmpCamera1?.startPreview()
                }

                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    if(rtmpCamera1?.isStreaming == true){
                        rtmpCamera1?.stopStream()
                        switchSwitchBroadcastStateButtonState(isBroadcastLive = false)
                    }
                    rtmpCamera1?.stopPreview()
                }
            })

            setOnTouchListener { v, event ->
                if (event.pointerCount > 1 && event.action == MotionEvent.ACTION_MOVE) {
                    rtmpCamera1?.setZoom(event)
                } else if (event.action == MotionEvent.ACTION_DOWN) {
                    rtmpCamera1?.tapToFocus(view, event)
                } else if (event.action == MotionEvent.ACTION_UP){
                    v.performClick()
                }

                true
            }
        }
    }

    private fun initializeToast() {
        val context = viewBinding?.root?.context ?: return
        rtmpConnectCheckers.add(object: ConnectCheckerRtmp{
            override fun onAuthErrorRtmp() {
                Completable
                    .fromRunnable {
                        Toast.makeText(
                            context,
                            R.string.fragment_live_broadcasting_error_auth_error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .subscribeOn(mainThreadScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(viewScopeDisposables)
            }

            override fun onAuthSuccessRtmp() {
                Completable
                    .fromRunnable {
                        Toast.makeText(
                            context,
                            R.string.fragment_live_broadcasting_message_auth_success,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .subscribeOn(mainThreadScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(viewScopeDisposables)
            }

            override fun onConnectionFailedRtmp(reason: String) {
                Completable
                    .fromRunnable {
                        val errorMessage = getString(R.string.fragment_live_broadcasting_error_connection_failed, reason)
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                    .subscribeOn(mainThreadScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(viewScopeDisposables)
            }

            override fun onConnectionStartedRtmp(rtmpUrl: String) = Unit

            override fun onConnectionSuccessRtmp() {
                Completable
                    .fromRunnable {
                        Toast.makeText(
                            context,
                            R.string.fragment_live_broadcasting_message_connection_succeeded,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .subscribeOn(mainThreadScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(viewScopeDisposables)
            }

            override fun onDisconnectRtmp() {
                Completable
                    .fromRunnable {
                        Toast.makeText(
                            context,
                            R.string.fragment_live_broadcasting_message_disconnected,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .subscribeOn(mainThreadScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(viewScopeDisposables)
            }

            override fun onNewBitrateRtmp(bitrate: Long) = Unit

        })
    }

    private fun switchSwitchBroadcastStateButtonState(isBroadcastLive: Boolean) {
        viewBinding?.switchBroadcastStateButton?.run {
            when(isBroadcastLive) {
                true -> {
                    text = getString(R.string.fragment_live_broadcasting_switch_broadcast_state_button_off)
                    icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_video_camera_off_24)
                }
                else -> {
                    text = getString(R.string.fragment_live_broadcasting_switch_broadcast_state_button_on)
                    icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_video_camera_on_24)
                }
            }
        }
    }

    private fun initializeSwitchBroadcastStateButton() {
        viewBinding?.run {
            switchBroadcastStateButton.run {
                rtmpConnectCheckers.add(object: ConnectCheckerRtmp{
                    override fun onAuthErrorRtmp() = Unit

                    override fun onAuthSuccessRtmp() = Unit

                    override fun onConnectionFailedRtmp(reason: String) {
                        Completable
                            .fromRunnable { switchSwitchBroadcastStateButtonState(isBroadcastLive = false) }
                            .subscribeOn(mainThreadScheduler)
                            .observeOn(mainThreadScheduler)
                            .subscribeBy(applicationErrorsLogger::logError)
                            .addTo(viewScopeDisposables)
                    }

                    override fun onConnectionStartedRtmp(rtmpUrl: String) = Unit

                    override fun onConnectionSuccessRtmp() = Unit

                    override fun onDisconnectRtmp() = Unit

                    override fun onNewBitrateRtmp(bitrate: Long) = Unit

                })

                clicks()
                    .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(
                        onNext = fun (_) {
                            val rtmpCamera1 = rtmpCamera1 ?: return
                            val endPointAddress = viewModel.endPointAddress.value ?: return

                            if(!rtmpCamera1.isStreaming) {
                                val sourceUserName = viewModel.sourceUserName.value
                                val sourcePassword = viewModel.sourcePassword.value
                                if(!sourceUserName.isNullOrEmpty() && !sourcePassword.isNullOrEmpty()) {
                                    rtmpCamera1.setAuthorization(sourceUserName, sourcePassword)
                                }
                                if(prepareEncoders()) {
                                    rtmpCamera1.startStream(endPointAddress)
                                    switchSwitchBroadcastStateButtonState(isBroadcastLive = true)
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.fragment_live_broadcasting_error_failed_to_prepare_encoders,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    switchSwitchBroadcastStateButtonState(isBroadcastLive = false)
                                }
                            } else {
                                rtmpCamera1.stopStream()
                                switchSwitchBroadcastStateButtonState(isBroadcastLive = false)
                            }
                        },
                        onError = applicationErrorsLogger::logError
                    )
                    .addTo(viewScopeDisposables)
            }
        }
    }

    private fun initializeCurrentVideoBitrate() {
        viewBinding?.currentVideoBitrate?.run {
            rtmpConnectCheckers.add(object: ConnectCheckerRtmp{
                override fun onAuthErrorRtmp() = Unit

                override fun onAuthSuccessRtmp() = Unit

                override fun onConnectionFailedRtmp(reason: String) = Unit

                override fun onConnectionStartedRtmp(rtmpUrl: String) = Unit

                override fun onConnectionSuccessRtmp() = Unit

                override fun onDisconnectRtmp() = Unit

                override fun onNewBitrateRtmp(bitrate: Long) {
                    Completable
                        .fromRunnable { text = getString(R.string.fragment_live_broadcasting_video_bitrate_text, bitrate) }
                        .subscribeOn(mainThreadScheduler)
                        .observeOn(mainThreadScheduler)
                        .subscribeBy(applicationErrorsLogger::logError)
                        .addTo(viewScopeDisposables)
                }

            })
        }
    }

    private fun initializeSwitchMicrophoneOnOffButton() {
        viewBinding?.switchMicrophoneOnOffButton?.run {
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {
                        if(rtmpCamera1?.isAudioMuted == true) {
                            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_mic_24)
                            rtmpCamera1?.enableAudio()
                        } else {
                            icon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_mic_off_24)
                            rtmpCamera1?.disableAudio()
                        }
                    }
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeSwitchCameraFacingButton() {
        viewBinding?.switchCameraFacingButton?.run {
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {
                        try {
                            rtmpCamera1?.switchCamera()
                        } catch (ex: CameraOpenException) {
                            Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
                        }
                    },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeSwitchCameraOnOffButton() {
        viewBinding?.switchCameraOnOffButton?.run {
            isEnabled = false
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {},
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun prepareEncoders(): Boolean {
        val rtmpCamera1 = rtmpCamera1 ?: return false
        val resolutionPosition = viewModel.preferredVideoResolutionPosition.value ?: return false
        val videoFps = viewModel.preferredVideoFps.value ?: return false
        val videoBitrate = viewModel.preferredVideoBitrate.value ?: return false
        val audioBitrate = viewModel.preferredAudioBitrate.value ?: return false
        val audioSampleRate = viewModel.preferredAudioSampleRate.value ?: return false
        val isMono = viewModel.preferredIsAudioChannelMono.value ?: return false
        val isEchoCancelerEnabled = viewModel.preferredIsEchoCancelerEnabled.value ?: return false
        val isNoiseSuppressorEnabled = viewModel.preferredIsNoiseSuppressorEnabled.value ?: return false
        val resolution = rtmpCamera1.resolutionsBack[resolutionPosition]
        val width = resolution.width
        val height = resolution.height
        return rtmpCamera1.prepareVideo(
            width,
            height,
            videoFps,
            videoBitrate * 1024,
            CameraHelper.getCameraOrientation(context)
        ) && rtmpCamera1.prepareAudio(
            audioBitrate * 1024,
            audioSampleRate,
            !isMono,
            isEchoCancelerEnabled,
            isNoiseSuppressorEnabled
        )
    }
}