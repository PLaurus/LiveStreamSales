package tv.wfc.livestreamsales.application.di.modules.subcomponents

import dagger.Module
import tv.wfc.livestreamsales.features.greeting.di.GreetingComponent
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.login.di.LogInComponent
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.splash.di.SplashComponent
import tv.wfc.livestreamsales.features.usersettings.di.UserSettingsComponent

@Module(subcomponents = [
    SplashComponent::class,
    GreetingComponent::class,
    LogInComponent::class,
    MainAppContentComponent::class,
    HomeComponent::class,
    MainPageComponent::class,
    LiveBroadcastComponent::class,
    ProductOrderComponent::class,
    UserSettingsComponent::class
])
class AppSubComponentsModule