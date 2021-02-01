package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser

import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.api.ApiModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.repository.RepositoryModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.rest.AuthorizedRestModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.storage.StorageModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.subscomponents.AuthorizedUserSubComponentsModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.MainComponent
import tv.wfc.livestreamsales.di.scopes.AuthorizedUserScope
import tv.wfc.livestreamsales.repository.logout.ILogOutRepository
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@AuthorizedUserScope
@Subcomponent(modules = [
    AuthorizedUserSubComponentsModule::class,
    AuthorizedRestModule::class,
    ApiModule::class,
    StorageModule::class,
    RepositoryModule::class,
])
interface AuthorizedUserComponent {
    companion object {
        internal const val DEPENDENCY_NAME_AUTHORIZATION_TOKEN = "AUTHORIZATION_TOKEN"
    }

    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @Named(DEPENDENCY_NAME_AUTHORIZATION_TOKEN)
            token:String
        ): AuthorizedUserComponent
    }

    fun mainComponent(): MainComponent.Factory

    fun logOutRepository(): ILogOutRepository
}