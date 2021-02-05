package tv.wfc.livestreamsales.features.authorizeduser.di.modules.repository

import tv.wfc.livestreamsales.features.authorizeduser.di.scope.AuthorizedUserFeatureScope
import tv.wfc.livestreamsales.features.authorizeduser.repository.logout.ILogOutRepository
import tv.wfc.livestreamsales.features.authorizeduser.repository.logout.LogOutRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @AuthorizedUserFeatureScope
    @Binds
    internal abstract fun provideLogOutRepository(
        logOutRepository: LogOutRepository
    ): ILogOutRepository
}