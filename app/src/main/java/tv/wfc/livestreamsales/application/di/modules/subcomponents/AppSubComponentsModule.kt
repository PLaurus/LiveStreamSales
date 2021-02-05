package tv.wfc.livestreamsales.application.di.modules.subcomponents

import dagger.Module
import tv.wfc.livestreamsales.features.authorizeduser.di.AuthorizedUserComponent
import tv.wfc.livestreamsales.features.greeting.di.GreetingComponent
import tv.wfc.livestreamsales.features.login.di.LogInComponent
import tv.wfc.livestreamsales.features.splash.di.SplashComponent

@Module(subcomponents = [
    SplashComponent::class,
    LogInComponent::class,
    GreetingComponent::class,
    AuthorizedUserComponent::class
])
class AppSubComponentsModule