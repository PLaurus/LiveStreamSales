package tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastInformation

class LiveBroadcastAdapter(
    liveBroadcastsDiffUtilCallback: DiffUtil.ItemCallback<BroadcastInformation>,
    private val imageLoader: ImageLoader,
    private val onItemClick: (broadcastId: Long) -> Unit
): ListAdapter<BroadcastInformation, LiveBroadcastViewHolder>(liveBroadcastsDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveBroadcastViewHolder {
        val liveBroadcastPage =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_live_broadcast_page, parent, false)

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