package tv.wfc.livestreamsales.application.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import tv.wfc.livestreamsales.application.di.modules.certificates.CertificatesModule
import tv.wfc.livestreamsales.application.di.modules.coil.CoilModule
import tv.wfc.livestreamsales.application.di.modules.database.DatabaseModule
import tv.wfc.livestreamsales.application.di.modules.datasource.AppDataSourceModule
import tv.wfc.livestreamsales.application.di.modules.datetime.DateTimeModule
import tv.wfc.livestreamsales.application.di.modules.entitymapper.EntityMapperModule
import tv.wfc.livestreamsales.application.di.modules.errorslogger.ErrorsLoggerModule
import tv.wfc.livestreamsales.application.di.modules.manager.ManagersModule
import tv.wfc.livestreamsales.application.di.modules.pusher.PusherModule
import tv.wfc.livestreamsales.application.di.modules.reactivex.ReactiveXModule
import tv.wfc.livestreamsales.application.di.modules.repository.AppRepositoryModule
import tv.wfc.livestreamsales.application.di.modules.rest.RestModule
import tv.wfc.livestreamsales.application.di.modules.restapi.ApiModule
import tv.wfc.livestreamsales.application.di.modules.serialization.SerializationModule
import tv.wfc.livestreamsales.application.di.modules.sharedpreferences.SharedPreferencesModule
import tv.wfc.livestreamsales.application.di.modules.subcomponents.AppSubComponentsModule
import tv.wfc.livestreamsales.application.di.modules.utils.UtilsModule
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.ViewModelProviderModule
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import tv.wfc.livestreamsales.features.authorization.di.AuthorizationComponent
import tv.wfc.livestreamsales.features.greeting.di.GreetingComponent
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.LiveBroadcastingComponent
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.LiveBroadcastingSettingsComponent
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.myorders.di.MyOrdersComponent
import tv.wfc.livestreamsales.features.mystreams.di.MyStreamsComponent
import tv.wfc.livestreamsales.features.needpaymentinformation.di.NeedPaymentInformationComponent
import tv.wfc.livestreamsales.features.orderediting.di.OrderDeliveryAddressEditingComponent
import tv.wfc.livestreamsales.features.orderediting.di.OrderEditingComponent
import tv.wfc.livestreamsales.features.orderinformation.di.OrderInformationComponent
import tv.wfc.livestreamsales.features.orderisconfirmed.di.OrderIsConfirmedComponent
import tv.wfc.livestreamsales.features.paymentcardinformation.di.PaymentCardInformationComponent
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.productsareordered.di.ProductsAreOrderedComponent
import tv.wfc.livestreamsales.features.profile.di.ProfileComponent
import tv.wfc.livestreamsales.features.splash.di.SplashComponent
import tv.wfc.livestreamsales.features.streamcreation.di.StreamCreationComponent
import tv.wfc.livestreamsales.features.streamediting.di.StreamEditingComponent

@ApplicationScope
@Component(modules = [
    AppSubComponentsModule::class,
    ReactiveXModule::class,
    ErrorsLoggerModule::class,
    RestModule::class,
    DatabaseModule::class,
    CertificatesModule::class,
    ManagersModule::class,
    SharedPreferencesModule::class,
    ViewModelProviderModule::class,
    ApiModule::class,
    AppDataSourceModule::class,
    AppRepositoryModule::class,
    CoilModule::class,
    UtilsModule::class,
    SerializationModule::class,
    EntityMapperModule::class,
    PusherModule::class,
    DateTimeModule::class
])
interface AppComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun splashComponent(): SplashComponent.Factory
    fun greetingComponent(): GreetingComponent.Factory
    fun authorizationComponent(): AuthorizationComponent.Factory
    fun mainAppContentComponent(): MainAppContentComponent.Factory
    fun homeComponent(): HomeComponent.Factory
    fun mainPageComponent(): MainPageComponent.Factory
    fun liveBroadcastComponent(): LiveBroadcastComponent.Factory
    fun productOrderComponent(): ProductOrderComponent.Factory
    fun profileComponent(): ProfileComponent.Factory
    fun productsAreOrderedComponent(): ProductsAreOrderedComponent.Factory
    fun paymentCardInformationComponent(): PaymentCardInformationComponent.Factory
    fun myOrdersComponent(): MyOrdersComponent.Factory
    fun needPaymentInformationComponent(): NeedPaymentInformationComponent.Factory
    fun orderInformationComponent(): OrderInformationComponent.Factory
    fun orderEditingComponent(): OrderEditingComponent.Factory
    fun orderDeliveryAddressEditingComponent() : OrderDeliveryAddressEditingComponent.Factory
    fun orderIsConfirmedComponent(): OrderIsConfirmedComponent.Factory
    fun liveBroadcastingSettingsComponent(): LiveBroadcastingSettingsComponent.Factory
    fun liveBroadcastingComponent(): LiveBroadcastingComponent.Factory
    fun myBroadcastsComponent(): MyStreamsComponent.Builder
    fun streamCreationComponentFactory(): StreamCreationComponent.Factory
    fun streamEditingComponentFactory(): StreamEditingComponent.Factory
}