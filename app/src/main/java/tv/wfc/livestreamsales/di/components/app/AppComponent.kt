package tv.wfc.livestreamsales.di.components.app

import android.content.Context
import tv.wfc.livestreamsales.di.components.app.modules.applicationsettings.ApplicationSettingsModule
import tv.wfc.livestreamsales.di.components.app.modules.authorization.AuthorizationModule
import tv.wfc.livestreamsales.di.components.app.modules.certificates.CertificatesModule
import tv.wfc.livestreamsales.di.components.app.modules.database.SharedPreferencesModule
import tv.wfc.livestreamsales.di.components.app.modules.errorslogger.ErrorsLoggerModule
import tv.wfc.livestreamsales.di.components.app.modules.reactivex.ReactiveXModule
import tv.wfc.livestreamsales.di.components.app.modules.rest.RestModule
import tv.wfc.livestreamsales.di.components.app.modules.subcomponents.AppSubComponentsModule
import tv.wfc.livestreamsales.di.components.app.modules.utils.UtilsModule
import tv.wfc.livestreamsales.di.components.app.modules.viewmodelprovider.ViewModelProviderModule
import tv.wfc.livestreamsales.di.components.app.subscomponents.greeting.GreetingComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.LogInComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.splash.SplashComponent
import tv.wfc.livestreamsales.di.scopes.ApplicationScope
import tv.wfc.livestreamsales.repository.authorization.IAuthorizationRepository
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
    fun authorizationComponent(): LogInComponent.Factory

    fun authorizationRepository(): IAuthorizationRepository
}