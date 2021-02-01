package com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.repository

import com.example.livestreamsales.di.scopes.AuthorizedUserScope
import com.example.livestreamsales.repository.logout.ILogOutRepository
import com.example.livestreamsales.repository.logout.LogOutRepository
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