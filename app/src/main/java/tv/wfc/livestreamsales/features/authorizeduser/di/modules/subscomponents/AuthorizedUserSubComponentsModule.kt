package tv.wfc.livestreamsales.features.authorizeduser.di.modules.subscomponents

import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import dagger.Module
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent

@Module(subcomponents = [
    MainAppContentComponent::class,
    HomeComponent::class,
    MainPageComponent::class,
    LiveBroadcastComponent::class
])
class AuthorizedUserSubComponentsModule