package tv.wfc.livestreamsales.features.orderediting.di

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.orderediting.di.modules.orderdeliveryaddressediting.viewmodel.OrderDeliveryAddressEditingViewModelModule
import tv.wfc.livestreamsales.features.orderediting.di.qualifiers.OrderDeliveryAddressEditingViewModelStoreOwner
import tv.wfc.livestreamsales.features.orderediting.di.qualifiers.OrderEditingViewModelStoreOwner
import tv.wfc.livestreamsales.features.orderediting.di.scope.OrderDeliveryAddressEditingFeatureScope

@OrderDeliveryAddressEditingFeatureScope
@Subcomponent(modules = [
    OrderDeliveryAddressEditingViewModelModule::class
])
interface OrderDeliveryAddressEditingComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @OrderDeliveryAddressEditingViewModelStoreOwner
            orderDeliveryAddressEditingViewModelStoreOwner: ViewModelStoreOwner,
            @BindsInstance
            @OrderEditingViewModelStoreOwner
            orderEditingViewModelStoreOwner: ViewModelStoreOwner
        ): OrderDeliveryAddressEditingComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.orderediting.ui.deliveryaddressediting.OrderDeliveryAddressEditingFragment)
}