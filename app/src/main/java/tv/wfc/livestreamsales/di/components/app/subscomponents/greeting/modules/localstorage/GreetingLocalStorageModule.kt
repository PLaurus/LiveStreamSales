package tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.localstorage

import tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.localstorage.qualifiers.GreetingPageLocalStorage
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.storage.greetingpage.IGreetingPageStorage
import dagger.Binds
import dagger.Module

@Module
abstract class GreetingLocalStorageModule {
    @ActivityScope
    @Binds
    @GreetingPageLocalStorage
    internal abstract fun bindGreetingPageLocalStorage(
        greetingPageLocalStorage: tv.wfc.livestreamsales.storage.greetingpage.local.GreetingPageLocalStorage
    ): IGreetingPageStorage
}