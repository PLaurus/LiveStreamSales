package tv.wfc.livestreamsales.features.authorizeduser.di.modules.userinformation

import tv.wfc.livestreamsales.features.authorizeduser.di.modules.rest.qualifiers.AuthorizedApiProvider
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.userinformation.qualifiers.UserInformationLocalStorage
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.userinformation.qualifiers.UserInformationRemoteStorage
import tv.wfc.livestreamsales.features.authorizeduser.di.scope.AuthorizedUserFeatureScope
import tv.wfc.livestreamsales.features.rest.IApiProvider
import tv.wfc.livestreamsales.features.rest.api.authorized.IUserInformationApi
import tv.wfc.livestreamsales.features.authorizeduser.repository.userinformation.IUserInformationRepository
import tv.wfc.livestreamsales.features.authorizeduser.repository.userinformation.UserInformationRepository
import tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.IUserInformationStorage
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

    @AuthorizedUserFeatureScope
    @Binds
    @UserInformationRemoteStorage
    internal abstract fun provideUserInformationRemoteStorage(
        userInformationInformationRemoteStorage: tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.remote.UserInformationRemoteStorage
    ): IUserInformationStorage

    @AuthorizedUserFeatureScope
    @Binds
    @UserInformationLocalStorage
    internal abstract fun provideUserInformationLocalStorage(
        userInformationInformationLocalStorage: tv.wfc.livestreamsales.features.authorizeduser.storage.userinformation.local.UserInformationLocalStorage
    ): IUserInformationStorage

    @AuthorizedUserFeatureScope
    @Binds
    internal abstract fun provideUserInformationRepository(
        userInformationRepository: UserInformationRepository
    ): IUserInformationRepository
}