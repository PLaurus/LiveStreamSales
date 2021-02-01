package com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.storage

import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.storage.qualifiers.LogOutRemoteStorage
import com.example.livestreamsales.di.scopes.AuthorizedUserScope
import com.example.livestreamsales.storage.logout.ILogOutStorage
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {
    @AuthorizedUserScope
    @Binds
    @LogOutRemoteStorage
    internal abstract fun provideLogOutRemoteStorage(
        logOutRemoteStorage: com.example.livestreamsales.storage.logout.remote.LogOutRemoteStorage
    ): ILogOutStorage
}