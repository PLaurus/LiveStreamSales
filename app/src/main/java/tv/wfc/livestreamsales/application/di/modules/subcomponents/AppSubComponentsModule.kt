package tv.wfc.livestreamsales.application.di.modules.subcomponents

import dagger.Module
import tv.wfc.livestreamsales.features.authorizeduser.di.AuthorizedUserComponent
import tv.wfc.livestreamsales.features.greeting.di.GreetingComponent
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.login.di.LogInComponent
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.splash.di.SplashComponent

@Module(subcomponents = [
    // Sub components of AppComponent only
    // (Not) Authorized users are able to use them.
    SplashComponent::class,
    GreetingComponent::class,

    // Only for not authorized users
    LogInComponent::class,

    // Only for authorized users
    AuthorizedUserComponent::class,

    // Sub components of both AppComponent and AuthorizedUserComponent
    // (Not) Authorized users are able to use them.
    MainAppContentComponent::class,
    HomeComponent::class,
    MainPageComponent::class,
    LiveBroadcastComponent::class,
    ProductOrderComponent::class,
])
class AppSubComponentsModule