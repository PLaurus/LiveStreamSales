package tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.laurus.p.tools.livedata.LiveEvent
import com.laurus.p.tools.reactivex.NullablesWrapper
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.base.viewmodel.BaseViewModel
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.repository.livebroadcastingsettings.ILiveBroadcastingSettingsRepository
import tv.wfc.livestreamsales.application.tools.camera.ICameraPreviewSizeReceivingBehavior
import tv.wfc.livestreamsales.application.tools.camera.model.CameraSize
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.MinimalAudioBitrate
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.MinimalAudioSampleRate
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.MinimalVideoBitrate
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.qualifiers.MinimalVideoFps
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.error.*
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.model.navigation.NextDestination
import javax.inject.Inject

class LiveBroadcastingSettingsViewModel @Inject constructor(
    private val authorizationManager: IAuthorizationManager,
    private val liveBroadcastingSettingsRepository: ILiveBroadcastingSettingsRepository,
    private val cameraPreviewSizeReceivingBehavior: ICameraPreviewSizeReceivingBehavior,
    @MinimalVideoBitrate
    private val minimalVideoBitrate: Int,
    @MinimalVideoFps
    private val minimalVideoFps: Int,
    @MinimalAudioBitrate
    private val minimalAudioBitrate: Int,
    @MinimalAudioSampleRate
    private val minimalAudioSampleRate: Int,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @ComputationScheduler
    private val computationScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): BaseViewModel(), ILiveBroadcastingSettingsViewModel {
    private val nextDestinationSubject = PublishSubject.create<NextDestination>()
    private val serverAddressSubject = BehaviorSubject.createDefault("rtmp://7b9a2f.entrypoint.cloud.wowza.com/app-9223Q936")
    private val serverAddressErrorSubject: Observable<NullablesWrapper<ServerAddressError>> = BehaviorSubject.createDefault<NullablesWrapper<ServerAddressError>>(NullablesWrapper(null)).apply{
        serverAddressSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { serverAddress ->
                    val serverAddressError = checkServerAddress(serverAddress)
                    onNext(NullablesWrapper(serverAddressError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val serverPortSubject = BehaviorSubject.createDefault(NullablesWrapper(1935))
    private val serverPortErrorSubject: Observable<NullablesWrapper<ServerPortError>> = BehaviorSubject.createDefault<NullablesWrapper<ServerPortError>>(NullablesWrapper(null)).apply{
        serverPortSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { serverPort ->
                    val serverPortError = checkServerPort(serverPort.value)
                    onNext(NullablesWrapper(serverPortError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val streamKeySubject = BehaviorSubject.createDefault("a3098ce9")
    private val streamKeyErrorSubject: Observable<NullablesWrapper<StreamKeyError>> = BehaviorSubject.createDefault<NullablesWrapper<StreamKeyError>>(NullablesWrapper(null)).apply{
        streamKeySubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { streamKey ->
                    val streamKeyError = checkStreamKey(streamKey)
                    onNext(NullablesWrapper(streamKeyError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val sourceUserNameSubject = BehaviorSubject.createDefault("client69645")
    private val sourceUserNameErrorSubject: Observable<NullablesWrapper<SourceUserNameError>> = BehaviorSubject.createDefault<NullablesWrapper<SourceUserNameError>>(NullablesWrapper(null)).apply{
        sourceUserNameSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { sourceUserName ->
                    val sourceUserNameError = checkSourceUserName(sourceUserName)
                    onNext(NullablesWrapper(sourceUserNameError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val sourcePasswordSubject = BehaviorSubject.createDefault("e0b136ae")
    private val sourcePasswordErrorSubject: Observable<NullablesWrapper<SourcePasswordError>> = BehaviorSubject.createDefault<NullablesWrapper<SourcePasswordError>>(NullablesWrapper(null)).apply{
        sourcePasswordSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { sourcePassword ->
                    val sourcePasswordError = checkSourcePassword(sourcePassword)
                    onNext(NullablesWrapper(sourcePasswordError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val videoResolutionsSubject = BehaviorSubject.createDefault(emptyList<CameraSize>())
    private val videoResolutionPositionSubject = BehaviorSubject.createDefault(0)
    private val videoBitrateSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val videoBitrateErrorSubject: Observable<NullablesWrapper<VideoBitrateError>> = BehaviorSubject.createDefault<NullablesWrapper<VideoBitrateError>>(NullablesWrapper(null)).apply{
        videoBitrateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { videoBitrate ->
                    val videoBitrateError = checkVideoBitrate(videoBitrate.value)
                    onNext(NullablesWrapper(videoBitrateError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val videoFpsSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val videoFpsErrorSubject: Observable<NullablesWrapper<VideoFpsError>> = BehaviorSubject.createDefault<NullablesWrapper<VideoFpsError>>(NullablesWrapper(null)).apply{
        videoFpsSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { videoFps ->
                    val videoFpsError = checkVideoFps(videoFps.value)
                    onNext(NullablesWrapper(videoFpsError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val audioBitrateSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val audioBitrateErrorSubject: Observable<NullablesWrapper<AudioBitrateError>> = BehaviorSubject.createDefault<NullablesWrapper<AudioBitrateError>>(NullablesWrapper(null)).apply{
        audioBitrateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { audioBitrate ->
                    val audioBitrateError = checkAudioBitrate(audioBitrate.value)
                    onNext(NullablesWrapper(audioBitrateError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val audioSampleRateSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val audioSampleRateErrorSubject: Observable<NullablesWrapper<AudioSampleRateError>> = BehaviorSubject.createDefault<NullablesWrapper<AudioSampleRateError>>(NullablesWrapper(null)).apply{
        audioSampleRateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { audioSampleRate ->
                    val audioSampleRateError = checkAudioSampleRate(audioSampleRate.value)
                    onNext(NullablesWrapper(audioSampleRateError))
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    private val isAudioChannelMonoSubject = BehaviorSubject.createDefault(false)
    private val isEchoCancelerEnabledSubject = BehaviorSubject.createDefault(false)
    private val isNoiseSuppressorEnabledSubject = BehaviorSubject.createDefault(false)
    private val doSettingsHaveErrorsSubject = BehaviorSubject.createDefault(false).apply{
        Observable
            .combineLatestArray<NullablesWrapper<*>, Boolean>(
                arrayOf(
                    serverAddressErrorSubject,
                    serverPortErrorSubject,
                    streamKeyErrorSubject,
                    sourceUserNameErrorSubject,
                    sourcePasswordErrorSubject,
                    videoBitrateErrorSubject,
                    videoFpsErrorSubject,
                    audioBitrateErrorSubject,
                    audioSampleRateErrorSubject
                )
            ){ errors ->
                var settingsContainErrors = false

                for(error in errors){
                    if((error as NullablesWrapper<*>).value != null){
                        settingsContainErrors = true
                        break
                    }
                }

                settingsContainErrors
            }
            .distinctUntilChanged()
            .subscribeOn(computationScheduler)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::onNext,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private var dataPreparationDisposable: Disposable? = null
    private var dataRefreshmentDisposable: Disposable? = null

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)

    override val isAnyOperationInProgress = MutableLiveData<Boolean>().apply {
        isAnyOperationInProgressObservable
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val nextDestinationEvent = LiveEvent<NextDestination>().apply{
        nextDestinationSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val serverAddress: LiveData<String> = MutableLiveData<String>().apply{
        serverAddressSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val serverAddressError: LiveData<ServerAddressError?> = MutableLiveData<ServerAddressError?>().apply{
        serverAddressErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val serverPort: LiveData<Int?> = MutableLiveData<Int?>().apply{
        serverPortSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val serverPortError: LiveData<ServerPortError?> = MutableLiveData<ServerPortError?>().apply{
        serverPortErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val streamKey: LiveData<String> = MutableLiveData<String>().apply{
        streamKeySubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val streamKeyError: LiveData<StreamKeyError?> = MutableLiveData<StreamKeyError?>().apply{
        streamKeyErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val sourceUserName: LiveData<String> = MutableLiveData<String>().apply{
        sourceUserNameSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val sourceUserNameError: LiveData<SourceUserNameError?> = MutableLiveData<SourceUserNameError?>().apply{
        sourceUserNameErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val sourcePassword: LiveData<String> = MutableLiveData<String>().apply{
        sourcePasswordSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val sourcePasswordError: LiveData<SourcePasswordError?> = MutableLiveData<SourcePasswordError?>().apply{
        sourcePasswordErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val videoResolutions: LiveData<List<CameraSize>> = MutableLiveData<List<CameraSize>>().apply{
        videoResolutionsSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val videoResolutionPosition: LiveData<Int> = MutableLiveData<Int>().apply{
        videoResolutionPositionSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val videoBitrate: LiveData<Int?> = MutableLiveData<Int?>().apply{
        videoBitrateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val videoBitrateError: LiveData<VideoBitrateError?> = MutableLiveData<VideoBitrateError?>().apply{
        videoBitrateErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val videoFps: LiveData<Int?> = MutableLiveData<Int?>().apply{
        videoFpsSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val videoFpsError: LiveData<VideoFpsError?> = MutableLiveData<VideoFpsError?>().apply{
        videoFpsErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val audioBitrate: LiveData<Int?> = MutableLiveData<Int?>().apply{
        audioBitrateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val audioBitrateError: LiveData<AudioBitrateError?> = MutableLiveData<AudioBitrateError?>().apply{
        audioBitrateErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val audioSampleRate: LiveData<Int?> = MutableLiveData<Int?>().apply{
        audioSampleRateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val audioSampleRateError: LiveData<AudioSampleRateError?> = MutableLiveData<AudioSampleRateError?>().apply{
        audioSampleRateErrorSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isAudioChannelMono: LiveData<Boolean> = MutableLiveData<Boolean>().apply{
        isAudioChannelMonoSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isEchoCancelerEnabled: LiveData<Boolean> = MutableLiveData<Boolean>().apply{
        isEchoCancelerEnabledSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val isNoiseSuppressorEnabled: LiveData<Boolean> = MutableLiveData<Boolean>().apply{
        isNoiseSuppressorEnabledSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val doSettingsHaveErrors: LiveData<Boolean> = MutableLiveData<Boolean>().apply{
        doSettingsHaveErrorsSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    init{
        closeWhenNotAuthorized()
        saveLiveBroadcastingSettingsToRepositoryWhenChanged()
    }

    override fun prepareData(){
        if(dataPreparationState.value == ViewModelPreparationState.DataIsPrepared) return
        dataPreparationDisposable?.dispose()
        dataPreparationDisposable = updateDataWithRepositoryData()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .subscribeBy(
                onComplete = { dataPreparationState.value = ViewModelPreparationState.DataIsPrepared },
                onError = {
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData(it.message)
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    override fun refreshData() {
        dataRefreshmentDisposable?.dispose()
        dataRefreshmentDisposable = updateDataWithRepositoryData()
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { incrementActiveOperationsCount() }
            .doOnTerminate(::decrementActiveOperationsCount)
            .subscribeBy(applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    override fun updateServerAddress(address: String?) {
        val newAddress = address ?: ""
        if(newAddress == serverAddressSubject.value) return
        serverAddressSubject.onNext(newAddress)
    }

    override fun updateServerPort(port: Int?) {
        if(port == serverPortSubject.value?.value) return
        serverPortSubject.onNext(NullablesWrapper(port))
    }

    override fun updateStreamKey(key: String?) {
        val newKey = key ?: ""
        if(newKey == streamKeySubject.value) return
        streamKeySubject.onNext(newKey)
    }

    override fun updateSourceUserName(userName: String?) {
        val newUserName = userName ?: ""
        if(newUserName == sourceUserNameSubject.value) return
        sourceUserNameSubject.onNext(newUserName)
    }

    override fun updateSourcePassword(password: String?) {
        val newPassword = password ?: ""
        if(newPassword == sourcePasswordSubject.value) return
        sourcePasswordSubject.onNext(newPassword)
    }

    override fun updateVideoResolutionPosition(position: Int) {
        if(position == videoResolutionPositionSubject.value) return
        videoResolutionPositionSubject.onNext(position)
    }

    override fun updateVideoBitrate(bitrate: Int?) {
        if(bitrate == videoBitrateSubject.value?.value) return
        videoBitrateSubject.onNext(NullablesWrapper(bitrate))
    }

    override fun updateVideoFps(fps: Int?) {
        if(fps == videoFpsSubject.value?.value) return
        videoFpsSubject.onNext(NullablesWrapper(fps))
    }

    override fun updateAudioBitrate(bitrate: Int?) {
        if(bitrate == audioBitrateSubject.value?.value) return
        audioBitrateSubject.onNext(NullablesWrapper(bitrate))
    }

    override fun updateAudioSampleRate(sampleRate: Int?) {
        if(sampleRate == audioSampleRateSubject.value?.value) return
        audioSampleRateSubject.onNext(NullablesWrapper(sampleRate))
    }

    override fun updateIsAudioChannelMono(isMono: Boolean) {
        if(isMono == isAudioChannelMonoSubject.value) return
        isAudioChannelMonoSubject.onNext(isMono)
    }

    override fun updateIsEchoCancelerEnabled(isEnabled: Boolean) {
        if(isEnabled == isEchoCancelerEnabledSubject.value) return
        isEchoCancelerEnabledSubject.onNext(isEnabled)
    }

    override fun updateIsNoiseSuppressorEnabled(isEnabled: Boolean) {
        if(isEnabled == isNoiseSuppressorEnabledSubject.value) return
        isNoiseSuppressorEnabledSubject.onNext(isEnabled)
    }

    override fun confirmSettings() {
        if(doSettingsHaveErrorsSubject.value != false) return
        val serverAddress = serverAddressSubject.value ?: return
        val serverPort = serverPortSubject.value?.value ?: return
        val streamKey = streamKeySubject.value ?: return
        val sourceUserName = sourceUserNameSubject.value ?: return
        val sourcePassword = sourcePasswordSubject.value ?: return

        intentToNavigateToLiveBroadcasting(
            serverAddress,
            serverPort,
            streamKey,
            sourceUserName,
            sourcePassword
        )
    }

    override fun intentToCloseCurrentDestination() {
        nextDestinationSubject.onNext(NextDestination.Close)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private fun updateDataWithRepositoryData(): Completable {
        return Completable
            .mergeArray(
                prepareVideoResolutions(),
                prepareVideoResolutionPosition(),
                prepareVideoBitrate(),
                prepareVideoFps(),
                prepareAudioBitrate(),
                prepareAudioSampleRate(),
                prepareIsAudioChannelMono(),
                prepareIsEchoCancelerEnabled(),
                prepareIsNoiseSuppressorEnabled()
            )
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareVideoResolutions(): Completable{
        return Single
            .fromCallable { cameraPreviewSizeReceivingBehavior.getBackCameraPreviewSizes() }
            .subscribeOn(computationScheduler)
            .flatMapCompletable { videoResolutions ->
                Completable.fromRunnable { videoResolutionsSubject.onNext(videoResolutions) }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareVideoResolutionPosition(): Completable{
        return liveBroadcastingSettingsRepository
            .getVideoResolutionPosition()
            .flatMapCompletable { videoResolutionPosition ->
                Completable.fromRunnable {
                    videoResolutionPositionSubject.onNext(videoResolutionPosition)
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareVideoBitrate(): Completable{
        return liveBroadcastingSettingsRepository
            .getVideoBitrate()
            .flatMapCompletable{ videoBitrate ->
                Completable.fromRunnable {
                    videoBitrateSubject.onNext(NullablesWrapper(videoBitrate))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareVideoFps(): Completable{
        return liveBroadcastingSettingsRepository
            .getVideoFps()
            .flatMapCompletable{ videoFps ->
                Completable.fromRunnable {
                    videoFpsSubject.onNext(NullablesWrapper(videoFps))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareAudioBitrate(): Completable{
        return liveBroadcastingSettingsRepository
            .getAudioBitrate()
            .flatMapCompletable{ audioBitrate ->
                Completable.fromRunnable {
                    audioBitrateSubject.onNext(NullablesWrapper(audioBitrate))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareAudioSampleRate(): Completable{
        return liveBroadcastingSettingsRepository
            .getAudioSampleRate()
            .flatMapCompletable{ audioSampleRate ->
                Completable.fromRunnable {
                    audioSampleRateSubject.onNext(NullablesWrapper(audioSampleRate))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareIsAudioChannelMono(): Completable{
        return liveBroadcastingSettingsRepository
            .getIsAudioChannelMono()
            .flatMapCompletable{ isMono ->
                Completable.fromRunnable {
                    isAudioChannelMonoSubject.onNext(isMono)
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareIsEchoCancelerEnabled(): Completable{
        return liveBroadcastingSettingsRepository
            .getIsEchoCancelerEnabled()
            .flatMapCompletable{ isEnabled ->
                Completable.fromRunnable {
                    isEchoCancelerEnabledSubject.onNext(isEnabled)
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareIsNoiseSuppressorEnabled(): Completable{
        return liveBroadcastingSettingsRepository
            .getIsNoiseSuppressorEnabled()
            .flatMapCompletable{ isEnabled ->
                Completable.fromRunnable{
                    isNoiseSuppressorEnabledSubject.onNext(isEnabled)
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun intentToNavigateToLiveBroadcasting(
        serverAddress: String,
        serverPort: Int,
        streamKey: String,
        sourceUserName: String,
        sourcePassword: String
    ) {
        val nextDestination = NextDestination.LiveBroadcasting(
            serverAddress,
            serverPort,
            streamKey,
            sourceUserName,
            sourcePassword
        )
        nextDestinationSubject.onNext(nextDestination)
    }

    private fun checkServerAddress(address: String?): ServerAddressError?{
        return when{
            address.isNullOrBlank() -> ServerAddressError.ValueIsRequired
            else -> null
        }
    }

    private fun checkServerPort(port: Int?): ServerPortError?{
        return when{
            port == null -> ServerPortError.ValueIsRequired
            port < 0 -> ServerPortError.PortMustBeUnsignedInteger
            else -> null
        }
    }

    private fun checkStreamKey(key: String?): StreamKeyError?{
        return when{
            key.isNullOrBlank() -> StreamKeyError.ValueIsRequired
            else -> null
        }
    }

    private fun checkSourceUserName(userName: String?): SourceUserNameError?{
        return when{
            userName.isNullOrBlank() -> SourceUserNameError.ValueIsRequired
            else -> null
        }
    }

    private fun checkSourcePassword(password: String?): SourcePasswordError?{
        return when{
            password.isNullOrBlank() -> SourcePasswordError.ValueIsRequired
            else -> null
        }
    }

    private fun checkVideoBitrate(bitrate: Int?): VideoBitrateError?{
        return when{
            bitrate == null -> VideoBitrateError.ValueIsRequired
            bitrate < minimalVideoBitrate -> VideoBitrateError.BitrateIsTooLow(minimalVideoBitrate)
            else -> null
        }
    }

    private fun checkVideoFps(fps: Int?): VideoFpsError? {
        return when{
            fps == null -> VideoFpsError.ValueIsRequired
            fps < minimalVideoFps -> VideoFpsError.FpsIsTooLow(minimalVideoFps)
            else -> null
        }
    }

    private fun checkAudioBitrate(bitrate: Int?): AudioBitrateError? {
        return when{
            bitrate == null -> AudioBitrateError.ValueIsRequired
            bitrate < minimalAudioBitrate -> AudioBitrateError.BitrateIsTooLow(minimalAudioBitrate)
            else -> null
        }
    }

    private fun checkAudioSampleRate(sampleRate: Int?): AudioSampleRateError? {
        return when{
            sampleRate == null -> AudioSampleRateError.ValueIsRequired
            sampleRate < minimalAudioSampleRate -> AudioSampleRateError.SampleRateIsTooLow(minimalAudioSampleRate)
            else -> null
        }
    }

    private fun closeWhenNotAuthorized(){
        authorizationManager
            .isUserLoggedIn
            .distinctUntilChanged()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { isUserLoggedIn ->
                    if(!isUserLoggedIn){
                        intentToCloseCurrentDestination()
                    }
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun saveLiveBroadcastingSettingsToRepositoryWhenChanged(){
        saveVideoResolutionPositionToRepositoryWhenChanged()
        saveVideoBitrateToRepositoryWhenChanged()
        saveVideoFpsToRepositoryWhenChanged()
        saveAudioBitrateToRepositoryWhenChanged()
        saveAudioSampleRateToRepositoryWhenChanged()
        saveIsAudioChannelMonoToRepositoryWhenChanged()
        saveIsEchoCancelerEnabledToRepositoryWhenChanged()
        saveIsNoiseSuppressorEnabledToRepositoryWhenChanged()
    }

    private fun saveVideoResolutionPositionToRepositoryWhenChanged(){
        onVideoResolutionPositionChanged{ position ->
            liveBroadcastingSettingsRepository
                .updateVideoResolutionPosition(position)
                .observeOn(mainThreadScheduler)
                .subscribeBy(applicationErrorsLogger::logError)
                .addTo(disposables)
        }
    }

    private fun saveVideoBitrateToRepositoryWhenChanged(){
        onVideoBitrateChanged { bitrate ->
            bitrate?.let{
                liveBroadcastingSettingsRepository
                    .updateVideoBitrate(it)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(disposables)
            }
        }
    }

    private fun saveVideoFpsToRepositoryWhenChanged(){
        onVideoFpsChanged { fps ->
            fps?.let{
                liveBroadcastingSettingsRepository
                    .updateVideoFps(it)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(disposables)
            }
        }
    }

    private fun saveAudioBitrateToRepositoryWhenChanged(){
        onAudioBitrateChanged { audioBitrate ->
            audioBitrate?.let{
                liveBroadcastingSettingsRepository
                    .updateAudioBitrate(it)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(disposables)
            }
        }
    }

    private fun saveAudioSampleRateToRepositoryWhenChanged(){
        onAudioSampleRateChanged { audioSampleRate ->
            audioSampleRate?.let{
                liveBroadcastingSettingsRepository
                    .updateAudioSampleRate(it)
                    .observeOn(mainThreadScheduler)
                    .subscribeBy(applicationErrorsLogger::logError)
                    .addTo(disposables)
            }
        }
    }

    private fun saveIsAudioChannelMonoToRepositoryWhenChanged(){
        onIsAudioChannelMonoChanged { isMono ->
            liveBroadcastingSettingsRepository
                .updateIsAudioChannelMono(isMono)
                .observeOn(mainThreadScheduler)
                .subscribeBy(applicationErrorsLogger::logError)
                .addTo(disposables)
        }
    }

    private fun saveIsEchoCancelerEnabledToRepositoryWhenChanged(){
        onIsEchoCancelerEnabledChanged { isEnabled ->
            liveBroadcastingSettingsRepository
                .updateIsEchoCancelerEnabled(isEnabled)
                .observeOn(mainThreadScheduler)
                .subscribeBy(applicationErrorsLogger::logError)
                .addTo(disposables)
        }
    }

    private fun saveIsNoiseSuppressorEnabledToRepositoryWhenChanged(){
        onIsNoiseSuppressorEnabledChanged { isEnabled ->
            liveBroadcastingSettingsRepository
                .updateIsNoiseSuppressorEnabled(isEnabled)
                .observeOn(mainThreadScheduler)
                .subscribeBy(applicationErrorsLogger::logError)
                .addTo(disposables)
        }
    }

    private fun onVideoResolutionPositionChanged(onChanged: (position: Int) -> Unit){
        videoResolutionPositionSubject
            .skip(1)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { onChanged(it) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun onVideoBitrateChanged(onChanged: (bitrate: Int?) -> Unit){
        videoBitrateSubject
            .skip(1)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { onChanged(it.value) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun onVideoFpsChanged(onChanged: (fps: Int?) -> Unit){
        videoFpsSubject
            .skip(1)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { onChanged(it.value) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun onAudioBitrateChanged(onChanged: (audioBitrate: Int?) -> Unit){
        audioBitrateSubject
            .skip(1)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { onChanged(it.value) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun onAudioSampleRateChanged(onChanged: (audioSampleRate: Int?) -> Unit){
        audioSampleRateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { onChanged(it.value) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun onIsAudioChannelMonoChanged(onChanged: (isMono: Boolean) -> Unit){
        isAudioChannelMonoSubject
            .skip(1)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { onChanged(it) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun onIsEchoCancelerEnabledChanged(onChanged: (isEnabled: Boolean) -> Unit){
        isEchoCancelerEnabledSubject
            .skip(1)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { onChanged(it) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun onIsNoiseSuppressorEnabledChanged(onChanged: (isEnabled: Boolean) -> Unit){
        isNoiseSuppressorEnabledSubject
            .skip(1)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { onChanged(it) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}