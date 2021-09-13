package tv.wfc.livestreamsales.features.liveBroadcastingDestination.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laurus.p.tools.livedata.LiveEvent
import com.laurus.p.tools.reactivex.NullablesWrapper
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.repository.livebroadcastingsettings.ILiveBroadcastingSettingsRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.model.navigation.NextDestination
import javax.inject.Inject

class LiveBroadcastingViewModel @Inject constructor(
    private val liveBroadcastingSettingsRepository: ILiveBroadcastingSettingsRepository,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @ComputationScheduler
    private val computationScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), ILiveBroadcastingViewModel {
    private val disposables = CompositeDisposable()
    private val activeOperationsCountSubject = BehaviorSubject.createDefault(0)
    private val nextDestinationSubject = PublishSubject.create<NextDestination>()
    private val dataPreparationSubject = BehaviorSubject.createDefault<ViewModelPreparationState>(ViewModelPreparationState.DataIsNotPrepared)
    private val endPointAddressSubject = BehaviorSubject.createDefault<NullablesWrapper<String>>(NullablesWrapper(null))
    private val sourceUserNameSubject = BehaviorSubject.createDefault<NullablesWrapper<String>>(NullablesWrapper(null))
    private val sourcePasswordSubject = BehaviorSubject.createDefault<NullablesWrapper<String>>(NullablesWrapper(null))
    private val preferredVideoResolutionPositionSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val preferredVideoBitrateSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val preferredVideoFpsSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val preferredAudioBitrateSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val preferredAudioSampleRateSubject = BehaviorSubject.createDefault<NullablesWrapper<Int>>(NullablesWrapper(null))
    private val preferredIsAudioChannelMonoSubject = BehaviorSubject.createDefault<NullablesWrapper<Boolean>>(NullablesWrapper(null))
    private val preferredIsEchoCancelerEnabledSubject = BehaviorSubject.createDefault<NullablesWrapper<Boolean>>(NullablesWrapper(null))
    private val preferredIsNoiseSuppressorEnabledSubject = BehaviorSubject.createDefault<NullablesWrapper<Boolean>>(NullablesWrapper(null))

    private var dataPreparationDisposable: Disposable? = null

    override val isAnyOperationInProgress = MutableLiveData<Boolean>().apply {
        activeOperationsCountSubject
            .observeOn(mainThreadScheduler)
            .map { it > 0 }
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val nextDestinationEvent: LiveEvent<NextDestination> = LiveEvent<NextDestination>().apply{
        nextDestinationSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val endPointAddress: LiveData<String?> = MutableLiveData<String?>().apply {
        endPointAddressSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val sourceUserName: LiveData<String?> = MutableLiveData<String?>().apply {
        sourceUserNameSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val sourcePassword: LiveData<String?> = MutableLiveData<String?>().apply {
        sourcePasswordSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val preferredVideoResolutionPosition = MutableLiveData<Int?>().apply {
        preferredVideoResolutionPositionSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val preferredVideoBitrate = MutableLiveData<Int?>().apply {
        preferredVideoBitrateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val preferredVideoFps = MutableLiveData<Int?>().apply {
        preferredVideoFpsSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val preferredAudioBitrate = MutableLiveData<Int?>().apply {
        preferredAudioBitrateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val preferredAudioSampleRate = MutableLiveData<Int?>().apply {
        preferredAudioSampleRateSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val preferredIsAudioChannelMono = MutableLiveData<Boolean?>().apply {
        preferredIsAudioChannelMonoSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val preferredIsEchoCancelerEnabled = MutableLiveData<Boolean?>().apply {
        preferredIsEchoCancelerEnabledSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val preferredIsNoiseSuppressorEnabled = MutableLiveData<Boolean?>().apply {
        preferredIsNoiseSuppressorEnabledSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { value = it.value },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun prepareData(
        serverAddress: String,
        serverPort: Int,
        streamKey: String,
        sourceUserName: String,
        sourcePassword: String
    ) {
        if(dataPreparationSubject.value == ViewModelPreparationState.DataIsPrepared) return
        dataPreparationDisposable?.dispose()
        dataPreparationDisposable = Completable
            .mergeArray(
                prepareEndPointAddress(serverAddress, serverPort, streamKey),
                prepareSourceUserName(sourceUserName),
                prepareSourcePassword(sourcePassword),
                preparePreferredVideoResolutionPosition(),
                preparePreferredVideoBitrate(),
                preparePreferredVideoFps(),
                preparePreferredAudioBitrate(),
                preparePreferredAudioSampleRate(),
                preparePreferredIsAudioChannelMono(),
                preparePreferredIsEchoCancelerEnabled(),
                preparePreferredIsNoiseSuppressorEnabled()
            )
            .observeOn(mainThreadScheduler)
            .doOnSubscribe{ dataPreparationSubject.onNext(ViewModelPreparationState.DataIsBeingPrepared) }
            .subscribeBy(
                onComplete = { dataPreparationSubject.onNext(ViewModelPreparationState.DataIsPrepared) },
                onError = {
                    dataPreparationSubject.onNext(ViewModelPreparationState.FailedToPrepareData(it.message))
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    override val dataPreparationState: LiveData<ViewModelPreparationState> = MutableLiveData<ViewModelPreparationState>().apply{
        dataPreparationSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private fun prepareEndPointAddress(
        serverAddress: String,
        serverPort: Int,
        streamKey: String
    ): Completable {
        return Completable
            .fromRunnable {
                val endPointAddress = "$serverAddress/$streamKey"
                endPointAddressSubject.onNext(NullablesWrapper(endPointAddress))
            }
    }

    private fun prepareSourceUserName(sourceUserName: String): Completable {
        return Completable
            .fromRunnable {
                sourceUserNameSubject.onNext(NullablesWrapper(sourceUserName))
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun prepareSourcePassword(sourcePassword: String): Completable {
        return Completable
            .fromRunnable {
                sourcePasswordSubject.onNext(NullablesWrapper(sourcePassword))
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun preparePreferredVideoResolutionPosition(): Completable {
        return liveBroadcastingSettingsRepository
            .getVideoResolutionPosition()
            .flatMapCompletable { videoResolutionPosition ->
                Completable.fromRunnable {
                    preferredVideoResolutionPositionSubject.onNext(NullablesWrapper(videoResolutionPosition))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun preparePreferredVideoBitrate(): Completable {
        return liveBroadcastingSettingsRepository
            .getVideoBitrate()
            .flatMapCompletable { videoBitrate ->
                Completable.fromRunnable {
                    preferredVideoBitrateSubject.onNext(NullablesWrapper(videoBitrate))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun preparePreferredVideoFps(): Completable {
        return liveBroadcastingSettingsRepository
            .getVideoFps()
            .flatMapCompletable { videoFps ->
                Completable.fromRunnable {
                    preferredVideoFpsSubject.onNext(NullablesWrapper(videoFps))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun preparePreferredAudioBitrate(): Completable {
        return liveBroadcastingSettingsRepository
            .getAudioBitrate()
            .flatMapCompletable { audioBitrate ->
                Completable.fromRunnable {
                    preferredAudioBitrateSubject.onNext(NullablesWrapper(audioBitrate))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun preparePreferredAudioSampleRate(): Completable {
        return liveBroadcastingSettingsRepository
            .getAudioSampleRate()
            .flatMapCompletable { audioSampleRate ->
                Completable.fromRunnable {
                    preferredAudioSampleRateSubject.onNext(NullablesWrapper(audioSampleRate))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun preparePreferredIsAudioChannelMono(): Completable {
        return liveBroadcastingSettingsRepository
            .getIsAudioChannelMono()
            .flatMapCompletable { isAudioChannelMono ->
                Completable.fromRunnable {
                    preferredIsAudioChannelMonoSubject.onNext(NullablesWrapper(isAudioChannelMono))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun preparePreferredIsEchoCancelerEnabled(): Completable {
        return liveBroadcastingSettingsRepository
            .getIsEchoCancelerEnabled()
            .flatMapCompletable { isEchoCancelerEnabled ->
                Completable.fromRunnable {
                    preferredIsEchoCancelerEnabledSubject.onNext(NullablesWrapper(isEchoCancelerEnabled))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    private fun preparePreferredIsNoiseSuppressorEnabled(): Completable {
        return liveBroadcastingSettingsRepository
            .getIsNoiseSuppressorEnabled()
            .flatMapCompletable { isNoiseSuppressorEnabled ->
                Completable.fromRunnable {
                    preferredIsNoiseSuppressorEnabledSubject.onNext(NullablesWrapper(isNoiseSuppressorEnabled))
                }
            }
            .subscribeOn(mainThreadScheduler)
    }

    @Synchronized
    private fun incrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCountSubject.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount + 1

        activeOperationsCountSubject.onNext(newActiveOperationsCount)
    }

    @Synchronized
    private fun decrementActiveOperationsCount(){
        val currentActiveOperationsCount = activeOperationsCountSubject.value ?: 0
        val newActiveOperationsCount = currentActiveOperationsCount - 1

        activeOperationsCountSubject.onNext(newActiveOperationsCount)
    }
}