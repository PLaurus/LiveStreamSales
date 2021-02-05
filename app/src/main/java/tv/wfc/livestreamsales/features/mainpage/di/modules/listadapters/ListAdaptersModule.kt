package tv.wfc.livestreamsales.features.mainpage.di.modules.listadapters

import androidx.recyclerview.widget.ListAdapter
import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation
import tv.wfc.livestreamsales.features.mainpage.ui.adapters.announcements.AnnouncementViewHolder
import tv.wfc.livestreamsales.features.mainpage.ui.adapters.announcements.AnnouncementsAdapter
import tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast.LiveBroadcastAdapter
import tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast.LiveBroadcastViewHolder

@Module
abstract class ListAdaptersModule {
    @Binds
    internal abstract fun provideLiveBroadcastsAdapter(
        liveBroadcastAdapter: LiveBroadcastAdapter
    ): ListAdapter<BroadcastBaseInformation, LiveBroadcastViewHolder>

    @Binds
    internal abstract fun provideBroadcastAnnouncementsAdapter(
        announcementsAdapter: AnnouncementsAdapter
    ): ListAdapter<BroadcastBaseInformation, AnnouncementViewHolder>
}