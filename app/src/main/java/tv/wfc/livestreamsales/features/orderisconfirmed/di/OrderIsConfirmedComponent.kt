package tv.wfc.livestreamsales.features.orderisconfirmed.di

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.orderisconfirmed.di.modules.viewmodel.OrderIsConfirmedViewModelModule
import tv.wfc.livestreamsales.features.orderisconfirmed.di.qualifiers.OrderIsConfirmedViewModelStoreOwner
import tv.wfc.livestreamsales.features.orderisconfirmed.di.scope.OrderIsConfirmedFeatureScope
import tv.wfc.livestreamsales.features.orderisconfirmed.ui.OrderIsConfirmedDialogFragment

@OrderIsConfirmedFeatureScope
@Subcomponent(modules = [
    OrderIsConfirmedViewModelModule::class
])
interface OrderIsConfirmedComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @OrderIsConfirmedViewModelStoreOwner
            viewModelStoreOwner: ViewModelStoreOwner
        ): OrderIsConfirmedComponent
    }

    fun inject(fragment: OrderIsConfirmedDialogFragment)
}