package com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.userinformation

import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.rest.qualifiers.AuthorizedApiProvider
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.userinformation.qualifiers.UserInformationLocalStorage
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.userinformation.qualifiers.UserInformationRemoteStorage
import com.example.livestreamsales.di.scopes.AuthorizedUserScope
import com.example.livestreamsales.network.rest.IApiProvider
import com.example.livestreamsales.network.rest.api.authorized.IUserInformationApi
import com.example.livestreamsales.repository.userinformation.IUserInformationRepository
import com.example.livestreamsales.repository.userinformation.UserInformationRepository
import com.example.livestreamsales.storage.userinformation.IUserInformationStorage
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
        userInformationInformationRemoteStorage: com.example.livestreamsales.storage.userinformation.remote.UserInformationRemoteStorage
    ): IUserInformationStorage

    @AuthorizedUserScope
    @Binds
    @UserInformationLocalStorage
    internal abstract fun provideUserInformationLocalStorage(
        userInformationInformationLocalStorage: com.example.livestreamsales.storage.userinformation.local.UserInformationLocalStorage
    ): IUserInformationStorage

    @AuthorizedUserScope
    @Binds
    internal abstract fun provideUserInformationRepository(
        userInformationRepository: UserInformationRepository
    ): IUserInformationRepository
}