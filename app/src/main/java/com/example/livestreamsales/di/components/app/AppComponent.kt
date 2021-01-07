package com.example.livestreamsales.di.components.app

import android.content.Context
import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.di.components.mainactivity.MainActivityComponent
import com.example.livestreamsales.di.components.splash.SplashComponent
import com.example.livestreamsales.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [
    SubComponentsModule::class,
    ReactiveXModule::class,
    RestModule::class,
    AuthorizationModule::class,
    SharedPreferencesModule::class,
    ViewModelProviderModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun splashComponent(): SplashComponent.Factory
    fun authorizationManager(): IAuthorizationManager
    fun mainActivityComponent(): MainActivityComponent.Factory
}