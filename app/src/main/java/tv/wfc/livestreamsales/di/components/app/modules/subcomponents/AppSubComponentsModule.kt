package tv.wfc.livestreamsales.di.components.app.modules.subcomponents

import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.AuthorizedUserComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.GreetingComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.LogInComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.splash.SplashComponent
import dagger.Module

@Module(subcomponents = [
    SplashComponent::class,
    LogInComponent::class,
    GreetingComponent::class,
    AuthorizedUserComponent::class
])
class AppSubComponentsModule