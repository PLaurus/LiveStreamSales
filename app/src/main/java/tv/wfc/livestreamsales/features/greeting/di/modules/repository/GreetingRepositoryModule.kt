package tv.wfc.livestreamsales.features.greeting.di.modules.repository

import dagger.Binds
import dagger.Module
import tv.wfc.livestreamsales.features.greeting.di.scope.GreetingFeatureScope
import tv.wfc.livestreamsales.features.greeting.repository.GreetingPageRepository
import tv.wfc.livestreamsales.features.greeting.repository.IGreetingPageRepository

@Module
abstract class GreetingRepositoryModule {
    @GreetingFeatureScope
    @Binds
    internal abstract fun bindIGreetingPageRepository(
        greetingPageRepository: GreetingPageRepository
    ): IGreetingPageRepository
}