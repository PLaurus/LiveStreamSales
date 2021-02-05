package tv.wfc.livestreamsales.features.mainpage.ui.adapters.announcements

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation
import javax.inject.Inject

class AnnouncementsAdapter @Inject constructor(
    diffUtilsItemCallback: DiffUtil.ItemCallback<BroadcastBaseInformation>,
    private val imageLoader: ImageLoader
): ListAdapter<BroadcastBaseInformation, AnnouncementViewHolder>(diffUtilsItemCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementViewHolder {
        val announcementPage = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_broadcast_announcement_page, parent, false)

        return AnnouncementViewHolder(announcementPage, imageLoader)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val broadcastInformation = getItem(position)
        holder.bind(broadcastInformation)
    }
}