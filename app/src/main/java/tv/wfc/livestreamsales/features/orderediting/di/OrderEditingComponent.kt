package tv.wfc.livestreamsales.features.orderediting.di

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.orderediting.di.modules.orderediting.diffutils.DiffUtilsModule
import tv.wfc.livestreamsales.features.orderediting.di.modules.orderediting.viewmodel.OrderEditingViewModelModule
import tv.wfc.livestreamsales.features.orderediting.di.qualifiers.OrderEditingViewModelStoreOwner
import tv.wfc.livestreamsales.features.orderediting.di.scope.OrderEditingFeatureScope

@OrderEditingFeatureScope
@Subcomponent(modules = [
    OrderEditingViewModelModule::class,
    DiffUtilsModule::class
])
interface OrderEditingComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @OrderEditingViewModelStoreOwner
            viewModelStoreOwner: ViewModelStoreOwner
        ): OrderEditingComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.orderediting.ui.orderediting.OrderEditingFragment)
}