package tv.wfc.livestreamsales.features.livebroadcast.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation
import tv.wfc.livestreamsales.application.model.viewmodel.ViewModelPreparationState
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.IBroadcastsInformationRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.tools.exoplayer.PlaybackState
import tv.wfc.livestreamsales.application.tools.livedata.LiveEvent
import tv.wfc.livestreamsales.features.livebroadcast.repository.IBroadcastAnalyticsRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LiveBroadcastViewModel @Inject constructor(
    private val context: Context,
    @MainThreadScheduler
    private val mainThreadScheduler: Scheduler,
    @ComputationScheduler
    private val computationScheduler: Scheduler,
    private val imageLoader: ImageLoader,
    private val broadcastsInformationRepository: IBroadcastsInformationRepository,
    private val broadcastAnalyticsRepository: IBroadcastAnalyticsRepository,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), ILiveBroadcastViewModel {
    private val disposables = CompositeDisposable()

    private var broadcastId: Long? = null

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>()
    override val isDataBeingRefreshed = MutableLiveData(false)
    override val image = MutableLiveData<Drawable>()
    override val broadcastTitle = MutableLiveData<String>()
    override val viewersCount = MutableLiveData<Int>()
    override val broadcastDescription = MutableLiveData<String>()
    override val broadcastMediaItem = MutableLiveData<MediaItem?>()

    override val playbackState = MutableLiveData<PlaybackState>()
    override val onPlayerError = LiveEvent<ExoPlaybackException>()

    override val playerEventListener = object : Player.EventListener{
        override fun onPlaybackStateChanged(state: Int) {
            playbackState.value = PlaybackState.fromInt(state)
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            onPlayerError.value = error
            applicationErrorsLogger.logError(error)
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if(isPlaying){
                notifyUserIsWatchingBroadcast()
            } else{
                notifyUserIsNotWatchingBroadcast()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    override fun prepareData(broadcastId: Long){
        if(dataPreparationState.value != ViewModelPreparationState.DataIsNotPrepared &&
            dataPreparationState.value != null) return

        this.broadcastId = broadcastId

        prepareBroadcastInformation(broadcastId)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .subscribeBy(
                onComplete = {
                    dataPreparationState.value = ViewModelPreparationState.DataIsPrepared
                },
                onError = {
                    applicationErrorsLogger.logError(it)
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData()
                }
            )
            .addTo(disposables)
    }

    override fun refreshData() {
        if(isDataBeingRefreshed.value == true) return

        val broadcastId = this.broadcastId ?: return

        prepareBroadcastInformation(broadcastId)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { isDataBeingRefreshed.value = true }
            .subscribeBy(
                onComplete = { isDataBeingRefreshed.value = false },
                onError = {
                    applicationErrorsLogger.logError(it)
                    isDataBeingRefreshed.value = false
                }
            )
            .addTo(disposables)
    }

    private var userIsWatchingBroadcastDisposable: Disposable? = null

    override fun notifyUserIsWatchingBroadcast() {
        userIsWatchingBroadcastDisposable?.dispose()

        val broadcastId = this.broadcastId ?: return

        userIsWatchingBroadcastDisposable = Observable
            .interval(0L, 50L, TimeUnit.SECONDS, computationScheduler)
            .map{ }
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { notifyServerUserIsWatchingBroadcast(broadcastId) },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override fun notifyUserIsNotWatchingBroadcast() {
        userIsWatchingBroadcastDisposable?.dispose()
    }

    private fun prepareBroadcastInformation(broadcastId: Long): Completable {
        return broadcastsInformationRepository
            .getBroadcast(broadcastId)
            .flatMapCompletable{ broadcastInformation ->
                Completable.merge(listOf(
                    prepareImage(broadcastInformation),
                    prepareBroadcastTitle(broadcastInformation),
                    prepareViewersCount(broadcastInformation),
                    prepareBroadcastDescription(broadcastInformation),
                    prepareBroadcastMediaItem(broadcastInformation)
                ))
            }
    }

    private fun prepareImage(broadcastInformation: BroadcastInformation): Completable{
        return Completable.create { emitter ->
            val imageUri = broadcastInformation.imageUrl

            if(imageUri == null)
                emitter.onComplete()

            val request = ImageRequest.Builder(context)
                .data(imageUri)
                .placeholder(R.drawable.drawable_avatar_placeholder)
                .transformations(CircleCropTransformation())
                .target(
                    onStart = image::setValue,
                    onSuccess = { result ->
                        image.value = result
                        emitter.onComplete()
                    },
                    onError = {
                        emitter.onComplete() }
                )
                .build()

            val disposable = imageLoader.enqueue(request)
            emitter.setCancellable { disposable.dispose() }
        }
    }

    private fun prepareBroadcastTitle(broadcastInformation: BroadcastInformation): Completable{
        return Completable.create { emitter ->
            this.broadcastTitle.value = broadcastInformation.title
            emitter.onComplete()
        }
    }

    private fun prepareViewersCount(broadcastInformation: BroadcastInformation): Completable{
        return Completable.create { emitter ->
            val viewersCount = broadcastInformation.viewersCount

            if(viewersCount != null){
                this.viewersCount.value = viewersCount
            }

            emitter.onComplete()
        }
    }

    private fun prepareBroadcastDescription(
        broadcastInformation: BroadcastInformation
    ): Completable{
        return Completable.create{ emitter ->
            broadcastDescription.value =  broadcastInformation.description
            emitter.onComplete()
        }
    }

    private fun prepareBroadcastMediaItem(
        broadcastInformation: BroadcastInformation
    ): Completable{
        return Completable.fromRunnable{
            broadcastMediaItem.value = createBroadcastMediaItem(broadcastInformation)
        }
    }

    private fun createBroadcastMediaItem(
        broadcastInformation: BroadcastInformation
    ): MediaItem? {
        val manifestUrl = broadcastInformation.manifestUrl ?: return null

        return MediaItem.fromUri(manifestUrl)
    }

    private fun notifyServerUserIsWatchingBroadcast(broadcastId: Long){
        broadcastAnalyticsRepository.notifyWatchingBroadcast(broadcastId)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}