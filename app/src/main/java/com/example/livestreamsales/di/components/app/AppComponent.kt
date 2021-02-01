package com.example.livestreamsales.di.components.app

import android.content.Context
import com.example.livestreamsales.di.components.app.modules.applicationsettings.ApplicationSettingsModule
import com.example.livestreamsales.di.components.app.modules.authorization.AuthorizationModule
import com.example.livestreamsales.di.components.app.modules.certificates.CertificatesModule
import com.example.livestreamsales.di.components.app.modules.database.SharedPreferencesModule
import com.example.livestreamsales.di.components.app.modules.errorslogger.ErrorsLoggerModule
import com.example.livestreamsales.di.components.app.modules.reactivex.ReactiveXModule
import com.example.livestreamsales.di.components.app.modules.rest.RestModule
import com.example.livestreamsales.di.components.app.modules.subcomponents.AppSubComponentsModule
import com.example.livestreamsales.di.components.app.modules.utils.UtilsModule
import com.example.livestreamsales.di.components.app.modules.viewmodelprovider.ViewModelProviderModule
import com.example.livestreamsales.di.components.app.subscomponents.authorization.AuthorizationComponent
import com.example.livestreamsales.di.components.app.subscomponents.greeting.GreetingComponent
import com.example.livestreamsales.di.components.app.subscomponents.splash.SplashComponent
import com.example.livestreamsales.di.scopes.ApplicationScope
import com.example.livestreamsales.repository.authorization.IAuthorizationRepository
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [
    AppSubComponentsModule::class,
    ApplicationSettingsModule::class,
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
    fun greetingComponent(): GreetingComponent.Factory
    fun authorizationComponent(): AuthorizationComponent.Factory

    fun authorizationRepository(): IAuthorizationRepository
}