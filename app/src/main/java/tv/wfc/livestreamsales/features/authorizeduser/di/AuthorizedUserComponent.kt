package tv.wfc.livestreamsales.features.authorizeduser.di

import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.api.ApiModule
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.repository.RepositoryModule
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.rest.AuthorizedRestModule
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.storage.StorageModule
import tv.wfc.livestreamsales.features.authorizeduser.di.modules.subscomponents.AuthorizedUserSubComponentsModule
import tv.wfc.livestreamsales.features.authorizeduser.di.qualifiers.AuthorizationToken
import tv.wfc.livestreamsales.features.authorizeduser.di.scope.AuthorizedUserFeatureScope
import tv.wfc.livestreamsales.features.authorizeduser.repository.logout.ILogOutRepository
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.usersettings.di.UserSettingsComponent

@AuthorizedUserFeatureScope
@Subcomponent(modules = [
    AuthorizedUserSubComponentsModule::class,
    AuthorizedRestModule::class,
    ApiModule::class,
    StorageModule::class,
    RepositoryModule::class,
])
interface AuthorizedUserComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @AuthorizationToken
            token:String
        ): AuthorizedUserComponent
    }

    fun mainAppComponent(): MainAppContentComponent.Factory
    fun homeComponent(): HomeComponent.Factory
    fun mainPageComponent(): MainPageComponent.Factory
    fun liveBroadcastComponent(): LiveBroadcastComponent.Factory
    fun productOrderComponent(): ProductOrderComponent.Factory
    fun settingsComponent(): UserSettingsComponent.Factory

    fun logOutRepository(): ILogOutRepository
}