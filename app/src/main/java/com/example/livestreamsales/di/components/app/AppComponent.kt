package com.example.livestreamsales.di.components.app

import android.content.Context
import com.example.livestreamsales.authorization.IAuthorizationManager
import com.example.livestreamsales.di.components.app.modules.authorization.AuthorizationModule
import com.example.livestreamsales.di.components.app.modules.certificates.CertificatesModule
import com.example.livestreamsales.di.components.app.modules.errorslogger.ErrorsLoggerModule
import com.example.livestreamsales.di.components.app.modules.reactivex.ReactiveXModule
import com.example.livestreamsales.di.components.app.modules.rest.RestModule
import com.example.livestreamsales.di.components.app.modules.viewmodelprovider.ViewModelProviderModule
import com.example.livestreamsales.di.components.authorization.AuthorizationComponent
import com.example.livestreamsales.di.components.main.MainComponent
import com.example.livestreamsales.di.components.splash.SplashComponent
import com.example.livestreamsales.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [
    AppSubComponentsModule::class,
    ReactiveXModule::class,
    ErrorsLoggerModule::class,
    RestModule::class,
    CertificatesModule::class,
    AuthorizationModule::class,
    SharedPreferencesModule::class,
    ViewModelProviderModule::class,
    UtilsModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun splashComponent(): SplashComponent.Factory
    fun authorizationComponent(): AuthorizationComponent.Factory
    fun authorizationManager(): IAuthorizationManager
    fun mainActivityComponent(): MainComponent.Factory
}