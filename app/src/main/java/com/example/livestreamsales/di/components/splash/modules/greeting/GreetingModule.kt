package com.example.livestreamsales.di.components.splash.modules.greeting

import com.example.livestreamsales.di.components.splash.modules.greeting.qualifiers.GreetingLocalStorage
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.repository.greeting.GreetingRepository
import com.example.livestreamsales.repository.greeting.IGreetingRepository
import com.example.livestreamsales.storage.greeting.IGreetingStorage
import dagger.Binds
import dagger.Module

@Module
abstract class GreetingModule {
    @ActivityScope
    @Binds
    @GreetingLocalStorage
    internal abstract fun provideGreetingLocalStorage(
        greetingLocalStorage: com.example.livestreamsales.storage.greeting.local.GreetingLocalStorage
    ): IGreetingStorage

    @ActivityScope
    @Binds
    internal abstract fun provideGreetingRepository(
        greetingRepository: GreetingRepository
    ): IGreetingRepository
}