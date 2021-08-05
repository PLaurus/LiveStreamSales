package tv.wfc.livestreamsales.features.livebroadcast.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.laurus.p.tools.livedata.LiveEvent
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.chat.ChatMessage
import tv.wfc.livestreamsales.application.model.products.ProductGroup
import tv.wfc.livestreamsales.application.tools.exoplayer.PlaybackState

interface ILiveBroadcastViewModel: INeedPreparationViewModel {
    val isUserLoggedIn: LiveData<Boolean>

    val isDataBeingRefreshed: LiveData<Boolean>
    val genericErrorEvent: LiveEvent<String>

    val image: LiveData<Drawable>
    val broadcastTitle: LiveData<String>
    val viewersCount: LiveData<Int>
    val broadcastDescription: LiveData<String>

    val broadcastMediaItem: LiveData<MediaItem?>
    val playbackState: LiveData<PlaybackState>
    val onPlayerError: LiveEvent<ExoPlaybackException>
    val playerEventListener: Player.Listener

    val broadcastHasProducts: LiveData<Boolean>
    val productGroups: LiveData<List<ProductGroup>>

    val chatMessages: LiveData<List<ChatMessage>>
    val enteredMessage: LiveData<String>

    fun prepareData(broadcastId: Long)
    fun refreshData()
    fun notifyUserIsWatchingBroadcast()
    fun notifyUserIsNotWatchingBroadcast()

    fun updateEnteredMessage(message: String)
    fun sendMessage()
}