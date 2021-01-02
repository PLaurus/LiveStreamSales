package com.example.livestreamsales.di.components.app

import android.content.Context
import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [
    SubComponentsModule::class,
    ReactiveXModule::class,
    NetworkModule::class,
    AuthorizationModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun authorizationManager(): IAuthorizationManager
}