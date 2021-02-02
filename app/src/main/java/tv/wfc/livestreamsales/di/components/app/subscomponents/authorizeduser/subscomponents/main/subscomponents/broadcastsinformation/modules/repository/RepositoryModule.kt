package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.qualifiers.BroadcastsInformationFragmentScope
import tv.wfc.livestreamsales.repository.broadcastsinformation.BroadcastsInformationRepository
import tv.wfc.livestreamsales.repository.broadcastsinformation.IBroadcastsInformationRepository

@Module
abstract class RepositoryModule {
    @BroadcastsInformationFragmentScope
    @Binds
    internal abstract fun provideBroadcastsInformationRepository(
        broadcastsInformationRepository: BroadcastsInformationRepository
    ): IBroadcastsInformationRepository
}