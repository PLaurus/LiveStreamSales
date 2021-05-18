package tv.wfc.livestreamsales.application.di.modules.manager

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.application.manager.AuthorizationManager
import tv.wfc.livestreamsales.application.manager.IAuthorizationManager

@Module
abstract class ManagersModule {
    @ApplicationScope
    @Binds
    internal abstract fun bindAuthorizationManager(
        authorizationManager: AuthorizationManager
    ): IAuthorizationManager
}