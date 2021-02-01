package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.userinformation

import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.userinformation.qualifiers.UserInformationLocalStorage
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.userinformation.qualifiers.UserInformationRemoteStorage
import tv.wfc.livestreamsales.di.scopes.AuthorizedUserScope
import tv.wfc.livestreamsales.network.rest.IApiProvider
import tv.wfc.livestreamsales.network.rest.api.authorized.IUserInformationApi
import tv.wfc.livestreamsales.repository.userinformation.IUserInformationRepository
import tv.wfc.livestreamsales.repository.userinformation.UserInformationRepository
import tv.wfc.livestreamsales.storage.userinformation.IUserInformationStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class UserInformationModule {
    companion object{
        @Provides
        internal fun provideUserInformationApi(
            @AuthorizedApiProvider
            authorizedApiProvider: IApiProvider
        ): IUserInformationApi {
            return authorizedApiProvider.createApi(IUserInformationApi::class.java)
        }
    }

    @AuthorizedUserScope
    @Binds
    @UserInformationRemoteStorage
    internal abstract fun provideUserInformationRemoteStorage(
        userInformationInformationRemoteStorage: tv.wfc.livestreamsales.storage.userinformation.remote.UserInformationRemoteStorage
    ): IUserInformationStorage

    @AuthorizedUserScope
    @Binds
    @UserInformationLocalStorage
    internal abstract fun provideUserInformationLocalStorage(
        userInformationInformationLocalStorage: tv.wfc.livestreamsales.storage.userinformation.local.UserInformationLocalStorage
    ): IUserInformationStorage

    @AuthorizedUserScope
    @Binds
    internal abstract fun provideUserInformationRepository(
        userInformationRepository: UserInformationRepository
    ): IUserInformationRepository
}