package tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.broadcastinformation.Broadcast

class LiveBroadcastAdapter(
    liveBroadcastsDiffUtilCallback: DiffUtil.ItemCallback<Broadcast>,
    private val imageLoader: ImageLoader,
    private val onItemClick: (broadcastId: Long) -> Unit
): ListAdapter<Broadcast, LiveBroadcastViewHolder>(liveBroadcastsDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveBroadcastViewHolder {
        val liveBroadcastPage =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_live_broadcast_card, parent, false)

        return LiveBroadcastViewHolder(
            liveBroadcastPage,
            imageLoader,
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: LiveBroadcastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}