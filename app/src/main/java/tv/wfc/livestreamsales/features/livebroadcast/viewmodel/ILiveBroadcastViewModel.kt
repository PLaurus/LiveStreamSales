package tv.wfc.livestreamsales.features.livebroadcast.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.laurus.p.tools.livedata.LiveEvent
import tv.wfc.contentloader.viewmodel.IToBePreparedViewModel
import tv.wfc.livestreamsales.application.tools.exoplayer.PlaybackState

interface ILiveBroadcastViewModel: IToBePreparedViewModel {
    val isDataBeingRefreshed: LiveData<Boolean>
    val image: LiveData<Drawable>
    val broadcastTitle: LiveData<String>
    val viewersCount: LiveData<Int>
    val broadcastDescription: LiveData<String>
    val broadcastMediaItem: LiveData<MediaItem?>
    val playbackState: LiveData<PlaybackState>
    val onPlayerError: LiveEvent<ExoPlaybackException>

    val broadcastHasProducts: LiveData<Boolean>
    val firstProductPrice: LiveData<Float>
    val firstProductOldPrice: LiveData<Float>

    val playerEventListener: Player.EventListener

    fun prepareData(broadcastId: Long)
    fun refreshData()
    fun notifyUserIsWatchingBroadcast()
    fun notifyUserIsNotWatchingBroadcast()
}