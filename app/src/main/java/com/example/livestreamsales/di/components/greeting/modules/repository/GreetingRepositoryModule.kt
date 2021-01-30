package com.example.livestreamsales.di.components.greeting.modules.repository

import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.repository.greetingpage.GreetingPageRepository
import com.example.livestreamsales.repository.greetingpage.IGreetingPageRepository
import dagger.Binds
import dagger.Module

@Module
abstract class GreetingRepositoryModule {
    @ActivityScope
    @Binds
    internal abstract fun bindIGreetingPageRepository(
        greetingPageRepository: GreetingPageRepository
    ): IGreetingPageRepository
}