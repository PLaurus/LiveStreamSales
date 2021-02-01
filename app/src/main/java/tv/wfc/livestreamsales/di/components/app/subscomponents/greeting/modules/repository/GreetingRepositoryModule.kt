package tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.modules.repository

import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.repository.greetingpage.GreetingPageRepository
import tv.wfc.livestreamsales.repository.greetingpage.IGreetingPageRepository
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