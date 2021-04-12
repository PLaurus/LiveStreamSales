package tv.wfc.livestreamsales.application.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import tv.wfc.livestreamsales.application.di.modules.certificates.CertificatesModule
import tv.wfc.livestreamsales.application.di.modules.coil.CoilModule
import tv.wfc.livestreamsales.application.di.modules.errorslogger.ErrorsLoggerModule
import tv.wfc.livestreamsales.application.di.modules.optionals.api.OptionalApiModule
import tv.wfc.livestreamsales.application.di.modules.optionals.repository.OptionalRepositoryModule
import tv.wfc.livestreamsales.application.di.modules.optionals.rest.OptionalRestModule
import tv.wfc.livestreamsales.application.di.modules.optionals.storage.OptionalStorageModule
import tv.wfc.livestreamsales.application.di.modules.reactivex.ReactiveXModule
import tv.wfc.livestreamsales.application.di.modules.repository.AppRepositoryModule
import tv.wfc.livestreamsales.application.di.modules.rest.RestModule
import tv.wfc.livestreamsales.application.di.modules.restapi.ApiModule
import tv.wfc.livestreamsales.application.di.modules.sharedpreferences.SharedPreferencesModule
import tv.wfc.livestreamsales.application.di.modules.storage.AppStorageModule
import tv.wfc.livestreamsales.application.di.modules.subcomponents.AppSubComponentsModule
import tv.wfc.livestreamsales.application.di.modules.utils.UtilsModule
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.ViewModelProviderModule
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.application.repository.authorization.IAuthorizationRepository
import tv.wfc.livestreamsales.features.greeting.di.GreetingComponent
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.login.di.LogInComponent
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.splash.di.SplashComponent

@ApplicationScope
@Component(modules = [
    AppSubComponentsModule::class,
    ReactiveXModule::class,
    ErrorsLoggerModule::class,
    RestModule::class,
    OptionalRestModule::class,
    CertificatesModule::class,
    SharedPreferencesModule::class,
    ViewModelProviderModule::class,
    ApiModule::class,
    OptionalApiModule::class,
    AppStorageModule::class,
    OptionalStorageModule::class,
    AppRepositoryModule::class,
    OptionalRepositoryModule::class,
    CoilModule::class,
    UtilsModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun authorizationRepository(): IAuthorizationRepository

    // Sub components of AppComponent only
    // (Not) Authorized users are able to use them.
    fun splashComponent(): SplashComponent.Factory
    fun greetingComponent(): GreetingComponent.Factory

    // Only for not authorized users
    fun logInComponent(): LogInComponent.Factory

    // Sub components of both AppComponent and AuthorizedUserComponent
    // (Not) Authorized users are able to use them.
    fun mainAppContentComponent(): MainAppContentComponent.Factory
    fun homeComponent(): HomeComponent.Factory
    fun mainPageComponent(): MainPageComponent.Factory
    fun liveBroadcastComponent(): LiveBroadcastComponent.Factory
    fun productOrderComponent(): ProductOrderComponent.Factory
}