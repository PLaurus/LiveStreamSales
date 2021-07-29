package tv.wfc.livestreamsales.features.livebroadcast.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.laurus.p.tools.livedata.LiveEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import tv.wfc.contentloader.model.ViewModelPreparationState
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast
import tv.wfc.livestreamsales.application.model.chat.ChatMessage
import tv.wfc.livestreamsales.application.model.products.Product
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.IBroadcastsInformationRepository
import tv.wfc.livestreamsales.application.repository.chat.IChatRepository
import tv.wfc.livestreamsales.application.repository.products.IProductsRepository
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.tools.exoplayer.PlaybackState
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
    private val productsRepository: IProductsRepository,
    private val chatRepository: IChatRepository,
    private val authorizationManager: IAuthorizationManager,
    private val applicationErrorsLogger: IApplicationErrorsLogger
): ViewModel(), ILiveBroadcastViewModel {
    private val disposables = CompositeDisposable()
    private val productsSubject = PublishSubject.create<List<Product>>()

    private val maxChatMessages = 15

    private var broadcastId: Long? = null
    private var userIsWatchingBroadcastDisposable: Disposable? = null
    private var sendMessageDisposable: Disposable? = null

    override val dataPreparationState = MutableLiveData<ViewModelPreparationState>()
    override val isUserLoggedIn = MutableLiveData<Boolean>().apply{
        authorizationManager
            .isUserLoggedIn
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
    override val isDataBeingRefreshed = MutableLiveData(false)
    override val genericErrorEvent = LiveEvent<String>()
    override val image = MutableLiveData<Drawable>()
    override val broadcastTitle = MutableLiveData<String>()
    override val viewersCount = MutableLiveData<Int>()
    override val broadcastDescription = MutableLiveData<String>()
    override val broadcastMediaItem = MutableLiveData<MediaItem?>()

    override val playbackState = MutableLiveData<PlaybackState>()
    override val onPlayerError = LiveEvent<ExoPlaybackException>()

    override val playerEventListener = object : Player.Listener{
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

    override val broadcastHasProducts: LiveData<Boolean> = MutableLiveData(false).apply {
        productsSubject
            .observeOn(mainThreadScheduler)
            .map{ it.isNotEmpty() }
            .distinctUntilChanged()
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val products = MutableLiveData<List<Product>>().apply{
        productsSubject
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::setValue,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val chatMessages = MutableLiveData<List<ChatMessage>>().apply{
        fun addMessage(newMessage: ChatMessage){
            val newMessagesList = value?.toMutableList() ?: mutableListOf()

            newMessagesList.apply {
                val excessMessagesCount = ((size + 1) - maxChatMessages).coerceAtLeast(0)

                if(excessMessagesCount > 0){
                    repeat(excessMessagesCount){
                        removeLastOrNull()
                    }
                }

                add(0, newMessage)
            }

            value = newMessagesList
        }

        chatRepository
            .getChatObservable()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = ::addMessage,
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    override val enteredMessage = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    override fun prepareData(broadcastId: Long){
        if(dataPreparationState.value != ViewModelPreparationState.DataIsNotPrepared &&
            dataPreparationState.value != null) return

        this.broadcastId = broadcastId

        prepareBroadcastInformation(broadcastId)
            .concatWith(prepareProductsInformation(broadcastId))
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { dataPreparationState.value = ViewModelPreparationState.DataIsBeingPrepared }
            .doOnComplete { startAutoRefresh(10L) }
            .subscribeBy(
                onComplete = {
                    dataPreparationState.value = ViewModelPreparationState.DataIsPrepared
                },
                onError = {
                    dataPreparationState.value = ViewModelPreparationState.FailedToPrepareData()
                    applicationErrorsLogger.logError(it)
                }
            )
            .addTo(disposables)
    }

    override fun refreshData() {
        if(isDataBeingRefreshed.value == true) return
        if(dataPreparationState.value == ViewModelPreparationState.DataIsBeingPrepared) return

        val broadcastId = this.broadcastId ?: return

        prepareBroadcastInformation(broadcastId)
            .concatWith(prepareProductsInformation(broadcastId))
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

    override fun updateEnteredMessage(message: String) {
        if(enteredMessage.value == message) return
        enteredMessage.value = message
    }

    @Synchronized
    override fun sendMessage() {
        val message = enteredMessage.value ?: return

        sendMessageDisposable?.dispose()
        sendMessageDisposable = chatRepository
            .sendMessage(message)
            .observeOn(mainThreadScheduler)
            .doOnSubscribe { enteredMessage.value = "" }
            .subscribeBy(
                onError = {
                    applicationErrorsLogger.logError(it)

                    val errorMessage = context.getString(R.string.fragment_live_broadcast_error_failed_to_send_message)
                    genericErrorEvent.value = errorMessage
                }
            )
            .addTo(disposables)
    }

    private fun prepareBroadcastInformation(broadcastId: Long): Completable {
        return broadcastsInformationRepository
            .getBroadcast(broadcastId)
            .observeOn(mainThreadScheduler)
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

    private fun prepareImage(broadcast: Broadcast): Completable{
        return Completable.create { emitter ->
            val imageUri = broadcast.imageUrl

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
                    onError = { emitter.onComplete() }
                )
                .build()

            val disposable = imageLoader.enqueue(request)
            emitter.setCancellable { disposable.dispose() }
        }
    }

    private fun prepareBroadcastTitle(broadcast: Broadcast): Completable{
        return Completable.create { emitter ->
            this.broadcastTitle.value = broadcast.title
            emitter.onComplete()
        }
    }

    private fun prepareViewersCount(broadcast: Broadcast): Completable{
        return Completable.create { emitter ->
            val viewersCount = broadcast.viewersCount

            if(viewersCount != null){
                this.viewersCount.value = viewersCount
            }

            emitter.onComplete()
        }
    }

    private fun prepareBroadcastDescription(
        broadcast: Broadcast
    ): Completable{
        return Completable.create{ emitter ->
            broadcastDescription.value =  broadcast.description
            emitter.onComplete()
        }
    }

    private fun prepareBroadcastMediaItem(
        broadcast: Broadcast
    ): Completable{
        return Completable.fromRunnable{
            val previousManifestUri = broadcastMediaItem.value?.playbackProperties?.uri?.toString()
            val newBroadcastManifestUri = broadcast.manifestUrl

            if(newBroadcastManifestUri != null &&
                newBroadcastManifestUri == previousManifestUri) return@fromRunnable

            broadcastMediaItem.value = newBroadcastManifestUri?.let{
                createBroadcastMediaItem(it)
            }
        }
    }

    private fun prepareProductsInformation(broadcastId: Long): Completable{
        return Completable
            .create { emitter ->
                val disposable = productsRepository
                    .getProductGroups(broadcastId)
                    .lastOrError()
                    .map{ productGroups ->
                        productGroups
                            .map{ productGroup -> productGroup.toProducts() }
                            .map{ products -> products.toMutableList() }
                            .reduce{ result, nextProducts ->
                                result.apply{
                                    addAll(nextProducts)
                                }
                            }
                    }
                    .doOnTerminate { emitter.onComplete() }
                    .subscribeBy(
                        onSuccess = productsSubject::onNext,
                        onError = {
                            productsSubject.onNext(emptyList())
                            applicationErrorsLogger.logError(it)
                        }
                    )

                emitter.setDisposable(disposable)
            }
    }

    private fun createBroadcastMediaItem(
        broadcastManifestUrl: String
    ): MediaItem {
        return MediaItem.fromUri(broadcastManifestUrl)
    }

    private fun notifyServerUserIsWatchingBroadcast(broadcastId: Long){
        broadcastAnalyticsRepository.notifyWatchingBroadcast(broadcastId)
            .observeOn(mainThreadScheduler)
            .subscribeBy(applicationErrorsLogger::logError)
            .addTo(disposables)
    }

    private fun startAutoRefresh(period: Long, timeUnit: TimeUnit = TimeUnit.SECONDS){
        Observable
            .interval(period, timeUnit, computationScheduler)
            .map{ }
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = { refreshData() },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }
}