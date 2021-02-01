package com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.localstorage

import com.example.livestreamsales.di.components.app.subscomponents.greeting.modules.localstorage.qualifiers.GreetingPageLocalStorage
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.storage.greetingpage.IGreetingPageStorage
import dagger.Binds
import dagger.Module

@Module
abstract class GreetingLocalStorageModule {
    @ActivityScope
    @Binds
    @GreetingPageLocalStorage
    internal abstract fun bindGreetingPageLocalStorage(
        greetingPageLocalStorage: com.example.livestreamsales.storage.greetingpage.local.GreetingPageLocalStorage
    ): IGreetingPageStorage
}