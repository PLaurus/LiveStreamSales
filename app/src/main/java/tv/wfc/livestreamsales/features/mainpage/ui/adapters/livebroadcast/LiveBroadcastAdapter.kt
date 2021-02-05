package tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation
import javax.inject.Inject

class LiveBroadcastAdapter @Inject constructor(
    liveBroadcastsDiffUtilCallback: DiffUtil.ItemCallback<BroadcastBaseInformation>,
    private val imageLoader: ImageLoader
): ListAdapter<BroadcastBaseInformation, LiveBroadcastViewHolder>(liveBroadcastsDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveBroadcastViewHolder {
        val liveBroadcastPage =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_live_broadcast_page, parent, false)

        return LiveBroadcastViewHolder(liveBroadcastPage, imageLoader)
    }

    override fun onBindViewHolder(holder: LiveBroadcastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}