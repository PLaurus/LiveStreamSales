package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.repository

import tv.wfc.livestreamsales.di.scopes.AuthorizedUserScope
import tv.wfc.livestreamsales.repository.logout.ILogOutRepository
import tv.wfc.livestreamsales.repository.logout.LogOutRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @AuthorizedUserScope
    @Binds
    internal abstract fun provideLogOutRepository(
        logOutRepository: LogOutRepository
    ): ILogOutRepository
}