package tv.wfc.livestreamsales.application.di.modules.subcomponents

import dagger.Module
import tv.wfc.livestreamsales.features.greeting.di.GreetingComponent
import tv.wfc.livestreamsales.features.home.di.HomeComponent
import tv.wfc.livestreamsales.features.livebroadcast.di.LiveBroadcastComponent
import tv.wfc.livestreamsales.features.authorization.di.AuthorizationComponent
import tv.wfc.livestreamsales.features.broadcast_creation.di.BroadcastCreationComponent
import tv.wfc.livestreamsales.features.broadcast_editing.di.BroadcastEditingComponent
import tv.wfc.livestreamsales.features.liveBroadcastingDestination.di.LiveBroadcastingComponent
import tv.wfc.livestreamsales.features.liveBroadcastingSettingsDestination.di.LiveBroadcastingSettingsComponent
import tv.wfc.livestreamsales.features.mainappcontent.di.MainAppContentComponent
import tv.wfc.livestreamsales.features.mainpage.di.MainPageComponent
import tv.wfc.livestreamsales.features.my_broadcasts.di.MyBroadcastsComponent
import tv.wfc.livestreamsales.features.myorders.di.MyOrdersComponent
import tv.wfc.livestreamsales.features.needpaymentinformation.di.NeedPaymentInformationComponent
import tv.wfc.livestreamsales.features.orderediting.di.OrderDeliveryAddressEditingComponent
import tv.wfc.livestreamsales.features.orderediting.di.OrderEditingComponent
import tv.wfc.livestreamsales.features.orderinformation.di.OrderInformationComponent
import tv.wfc.livestreamsales.features.orderisconfirmed.di.OrderIsConfirmedComponent
import tv.wfc.livestreamsales.features.paymentcardinformation.di.PaymentCardInformationComponent
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.productsareordered.di.ProductsAreOrderedComponent
import tv.wfc.livestreamsales.features.splash.di.SplashComponent
import tv.wfc.livestreamsales.features.profile.di.ProfileComponent

@Module(subcomponents = [
    SplashComponent::class,
    GreetingComponent::class,
    AuthorizationComponent::class,
    MainAppContentComponent::class,
    HomeComponent::class,
    MainPageComponent::class,
    LiveBroadcastComponent::class,
    ProductOrderComponent::class,
    ProfileComponent::class,
    ProductsAreOrderedComponent::class,
    PaymentCardInformationComponent::class,
    MyOrdersComponent::class,
    NeedPaymentInformationComponent::class,
    OrderInformationComponent::class,
    OrderEditingComponent::class,
    OrderDeliveryAddressEditingComponent::class,
    OrderIsConfirmedComponent::class,
    LiveBroadcastingSettingsComponent::class,
    LiveBroadcastingComponent::class,
    MyBroadcastsComponent::class,
    BroadcastCreationComponent::class,
    BroadcastEditingComponent::class
])
class AppSubComponentsModule