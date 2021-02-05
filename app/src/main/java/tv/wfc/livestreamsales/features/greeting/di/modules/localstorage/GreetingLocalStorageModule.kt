package tv.wfc.livestreamsales.features.greeting.di.modules.localstorage

import tv.wfc.livestreamsales.features.greeting.di.modules.localstorage.qualifiers.GreetingPageLocalStorage
import tv.wfc.livestreamsales.features.greeting.storage.IGreetingPageStorage
import dagger.Binds
import dagger.Module

@Module
abstract class GreetingLocalStorageModule {
    @Binds
    @GreetingPageLocalStorage
    internal abstract fun bindGreetingPageLocalStorage(
        greetingPageLocalStorage: tv.wfc.livestreamsales.features.greeting.storage.local.GreetingPageLocalStorage
    ): IGreetingPageStorage
}