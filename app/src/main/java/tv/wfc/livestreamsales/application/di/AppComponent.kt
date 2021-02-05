package tv.wfc.livestreamsales.application.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import tv.wfc.livestreamsales.application.di.modules.certificates.CertificatesModule
import tv.wfc.livestreamsales.application.di.modules.coil.CoilModule
import tv.wfc.livestreamsales.application.di.modules.errorslogger.ErrorsLoggerModule
import tv.wfc.livestreamsales.application.di.modules.reactivex.ReactiveXModule
import tv.wfc.livestreamsales.application.di.modules.repository.AppRepositoryModule
import tv.wfc.livestreamsales.application.di.modules.rest.RestModule
import tv.wfc.livestreamsales.application.di.modules.restapi.NotAuthorizedRestApiModule
import tv.wfc.livestreamsales.application.di.modules.sharedpreferences.SharedPreferencesModule
import tv.wfc.livestreamsales.application.di.modules.storage.AppStorageModule
import tv.wfc.livestreamsales.application.di.modules.subcomponents.AppSubComponentsModule
import tv.wfc.livestreamsales.application.di.modules.utils.UtilsModule
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.ViewModelProviderModule
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.features.greeting.di.GreetingComponent
import tv.wfc.livestreamsales.features.login.di.LogInComponent
import tv.wfc.livestreamsales.features.splash.di.SplashComponent

@ApplicationScope
@Component(modules = [
    AppSubComponentsModule::class,
    ReactiveXModule::class,
    ErrorsLoggerModule::class,
    RestModule::class,
    CertificatesModule::class,
    SharedPreferencesModule::class,
    ViewModelProviderModule::class,
    NotAuthorizedRestApiModule::class,
    AppStorageModule::class,
    AppRepositoryModule::class,
    CoilModule::class,
    UtilsModule::class,
])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun splashComponent(): SplashComponent.Factory
    fun greetingComponent(): GreetingComponent.Factory

    fun logInComponent(): LogInComponent.Factory

    fun authorizationRepository(): IAuthorizationRepository
}