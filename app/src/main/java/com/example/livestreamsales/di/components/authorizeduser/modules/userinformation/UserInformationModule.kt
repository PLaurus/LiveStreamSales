package com.example.livestreamsales.di.components.authorizeduser.modules.userinformation

import com.example.livestreamsales.di.components.authorizeduser.modules.rest.qualifiers.AuthorizedApiProvider
import com.example.livestreamsales.di.components.authorizeduser.modules.userinformation.qualifiers.UserInformationLocalStorage
import com.example.livestreamsales.di.components.authorizeduser.modules.userinformation.qualifiers.UserInformationRemoteStorage
import com.example.livestreamsales.di.scopes.AuthorizedUserScope
import com.example.livestreamsales.network.rest.IApiProvider
import com.example.livestreamsales.network.rest.api.IUserApi
import com.example.livestreamsales.repository.user.IUserRepository
import com.example.livestreamsales.repository.user.UserRepository
import com.example.livestreamsales.storage.userinformation.IUserStorage
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
        ): IUserApi{
            return authorizedApiProvider.createApi(IUserApi::class.java)
        }
    }

    @AuthorizedUserScope
    @Binds
    @UserInformationRemoteStorage
    internal abstract fun provideUserInformationRemoteStorage(
        userInformationRemoteStorage: com.example.livestreamsales.storage.userinformation.remote.UserRemoteStorage
    ): IUserStorage

    @AuthorizedUserScope
    @Binds
    @UserInformationLocalStorage
    internal abstract fun provideUserInformationLocalStorage(
        userInformationLocalStorage: com.example.livestreamsales.storage.userinformation.local.UserLocalStorage
    ): IUserStorage

    @AuthorizedUserScope
    @Binds
    internal abstract fun provideUserInformationRepository(
        userInformationRepository: UserRepository
    ): IUserRepository
}