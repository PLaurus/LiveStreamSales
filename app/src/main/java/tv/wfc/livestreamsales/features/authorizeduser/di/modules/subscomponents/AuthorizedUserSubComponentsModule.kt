package tv.wfc.livestreamsales.features.authorizeduser.di.modules.subscomponents

import dagger.Module
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.usersettings.di.UserSettingsComponent

@Module(subcomponents = [
    // Sub components of both AppComponent and AuthorizedUserComponent
    // (Not) Authorized users are able to use them.
    MainAppContentComponent::class,
    HomeComponent::class,
    MainPageComponent::class,
    LiveBroadcastComponent::class,
    ProductOrderComponent::class,

    // Only for authorized users
    UserSettingsComponent::class
])
class AuthorizedUserSubComponentsModule