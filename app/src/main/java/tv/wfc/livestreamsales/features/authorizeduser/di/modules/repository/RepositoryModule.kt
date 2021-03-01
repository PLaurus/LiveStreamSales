package tv.wfc.livestreamsales.features.authorizeduser.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.authorizeduser.di.scope.AuthorizedUserFeatureScope
import tv.wfc.livestreamsales.features.authorizeduser.repository.logout.ILogOutRepository
import tv.wfc.livestreamsales.features.authorizeduser.repository.logout.LogOutRepository
import tv.wfc.livestreamsales.features.authorizeduser.repository.userinformation.IUserInformationRepository
import tv.wfc.livestreamsales.features.authorizeduser.repository.userinformation.UserInformationRepository

@Module
abstract class RepositoryModule {
    @AuthorizedUserFeatureScope
    @Binds
    internal abstract fun provideLogOutRepository(
        logOutRepository: LogOutRepository
    ): ILogOutRepository

    @AuthorizedUserFeatureScope
    @Binds
    internal abstract fun provideUserInformationRepository(
        userInformationRepository: UserInformationRepository
    ): IUserInformationRepository
}