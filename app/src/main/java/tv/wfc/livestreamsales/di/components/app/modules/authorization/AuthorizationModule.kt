package tv.wfc.livestreamsales.di.components.app.modules.authorization

import tv.wfc.livestreamsales.di.scopes.ApplicationScope
import tv.wfc.livestreamsales.network.rest.IApiProvider
import tv.wfc.livestreamsales.network.rest.api.notauthorized.ILogInApi
import tv.wfc.livestreamsales.repository.authorization.AuthorizationRepository
import tv.wfc.livestreamsales.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.storage.authorization.local.AuthorizationLocalStorage
import tv.wfc.livestreamsales.storage.authorization.local.IAuthorizationLocalStorage
import tv.wfc.livestreamsales.storage.authorization.remote.AuthorizationRemoteStorage
import tv.wfc.livestreamsales.storage.authorization.remote.IAuthorizationRemoteStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AuthorizationModule {

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationRemoteStorage(
        authorizationRemoteStorage: AuthorizationRemoteStorage
    ): IAuthorizationRemoteStorage

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationLocalStorage(
        authorizationLocalStorage: AuthorizationLocalStorage
    ): IAuthorizationLocalStorage

    @ApplicationScope
    @Binds
    abstract fun provideAuthorizationRepository(
        authorizationRepository: AuthorizationRepository
    ): IAuthorizationRepository

    companion object{
        @ApplicationScope
        @Provides
        @JvmStatic
        fun provideLogInApi(apiProvider: IApiProvider): ILogInApi {
            return apiProvider.createApi(ILogInApi::class.java)
        }
    }
}