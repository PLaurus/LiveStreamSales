package tv.wfc.livestreamsales.features.mainpage.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.BroadcastsInformationRepository
import tv.wfc.livestreamsales.application.repository.broadcastsinformation.IBroadcastsInformationRepository
import tv.wfc.livestreamsales.features.mainpage.di.scope.MainPageFeatureScope

@Module
abstract class RepositoryModule {
    @MainPageFeatureScope
    @Binds
    internal abstract fun provideBroadcastsInformationRepository(
        broadcastsInformationRepository: BroadcastsInformationRepository
    ): IBroadcastsInformationRepository
}