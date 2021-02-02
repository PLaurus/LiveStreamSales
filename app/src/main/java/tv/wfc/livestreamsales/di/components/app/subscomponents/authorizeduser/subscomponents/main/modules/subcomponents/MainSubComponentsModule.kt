package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.modules.subcomponents

import dagger.Module
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.broadcastsinformation.BroadcastsInformationComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.subscomponents.home.HomeComponent

@Module(subcomponents = [
    HomeComponent::class,
    BroadcastsInformationComponent::class
])
class MainSubComponentsModule