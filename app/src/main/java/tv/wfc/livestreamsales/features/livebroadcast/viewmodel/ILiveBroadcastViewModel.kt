package tv.wfc.livestreamsales.features.livebroadcast.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.laurus.p.tools.livedata.LiveEvent
import tv.wfc.contentloader.viewmodel.INeedPreparationViewModel
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
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
    val onPlayerError: LiveEvent<PlaybackException>
    val playerEventListener: Player.Listener

    val broadcastHasProducts: LiveData<Boolean>
    val broadcastHasTwoOrMoreProducts: LiveData<Boolean>
    val productGroups: LiveData<List<ProductGroup>>

    val streamChatMessages: LiveData<List<StreamChatMessage>>
    val enteredMessage: LiveData<String>

    fun prepareData(broadcastId: Long)
    fun refreshData()
    fun notifyUserIsWatchingBroadcast()
    fun notifyUserIsNotWatchingBroadcast()

    fun updateEnteredMessage(message: String)
    fun sendMessage()
}