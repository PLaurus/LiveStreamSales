package tv.wfc.livestreamsales.features.productsareordered.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.productsareordered.di.qualifiers.ProductsAreOrderedDialogFragment
import tv.wfc.livestreamsales.features.productsareordered.di.scope.ProductsAreOrderedFeatureScope
import tv.wfc.livestreamsales.features.productsareordered.viewmodel.IProductsAreOrderedViewModel
import tv.wfc.livestreamsales.features.productsareordered.viewmodel.ProductsAreOrderedViewModel

@Module
abstract class ProductsAreOrderedViewModelModule {
    companion object{
        @ProductsAreOrderedFeatureScope
        @Provides
        fun provideIProductsAreOrderedViewModel(
            @ProductsAreOrderedDialogFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IProductsAreOrderedViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[ProductsAreOrderedViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(ProductsAreOrderedViewModel::class)
    abstract fun bindProductsAreOrderedViewModelIntoMap(
        productsAreOrderedViewModel: ProductsAreOrderedViewModel
    ): ViewModel
}