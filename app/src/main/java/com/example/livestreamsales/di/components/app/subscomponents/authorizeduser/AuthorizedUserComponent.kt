package com.example.livestreamsales.di.components.app.subscomponents.authorizeduser

import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.api.ApiModule
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.repository.RepositoryModule
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.rest.AuthorizedRestModule
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.storage.StorageModule
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.modules.subscomponents.AuthorizedUserSubComponentsModule
import com.example.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.MainComponent
import com.example.livestreamsales.di.scopes.AuthorizedUserScope
import com.example.livestreamsales.repository.logout.ILogOutRepository
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