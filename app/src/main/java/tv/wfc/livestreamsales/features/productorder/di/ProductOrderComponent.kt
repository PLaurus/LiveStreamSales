package tv.wfc.livestreamsales.features.productorder.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.productorder.di.modules.diffutils.DiffUtilsModule
import tv.wfc.livestreamsales.features.productorder.di.modules.viewmodel.ProductOrderViewModelModule
import tv.wfc.livestreamsales.features.productorder.di.scope.ProductOrderFeature
import tv.wfc.livestreamsales.features.productorder.ui.ProductOrderDialogFragment

@ProductOrderFeature
@Subcomponent(modules = [
    ProductOrderViewModelModule::class,
    DiffUtilsModule::class
])
interface ProductOrderComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            productOrderFragment: Fragment
        ): ProductOrderComponent
    }

    fun inject(fragment: ProductOrderDialogFragment)
}