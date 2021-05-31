package tv.wfc.livestreamsales.features.productsareordered.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import tv.wfc.livestreamsales.features.productsareordered.di.modules.ProductsAreOrderedViewModelModule
import tv.wfc.livestreamsales.features.productsareordered.di.qualifiers.ProductsAreOrderedDialogFragment
import tv.wfc.livestreamsales.features.productsareordered.di.scope.ProductsAreOrderedFeatureScope

@ProductsAreOrderedFeatureScope
@Subcomponent(modules=[
    ProductsAreOrderedViewModelModule::class
])
interface ProductsAreOrderedComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance
            @ProductsAreOrderedDialogFragment
            fragment: Fragment
        ): ProductsAreOrderedComponent
    }

    fun inject(fragment: tv.wfc.livestreamsales.features.productsareordered.ui.ProductsAreOrderedDialogFragment)
}