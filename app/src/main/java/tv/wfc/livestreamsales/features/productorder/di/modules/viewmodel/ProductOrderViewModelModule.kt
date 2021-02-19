package tv.wfc.livestreamsales.features.productorder.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.productorder.di.scope.ProductOrderFeature
import tv.wfc.livestreamsales.features.productorder.viewmodel.IProductOrderViewModel
import tv.wfc.livestreamsales.features.productorder.viewmodel.ProductOrderViewModel

@Module
abstract class ProductOrderViewModelModule {
    companion object{
        @Provides
        @ProductOrderFeature
        @JvmStatic
        fun provideIProductOrderViewModel(
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IProductOrderViewModel{
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[ProductOrderViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(ProductOrderViewModel::class)
    abstract fun bindProductOrderViewModelIntoMap(
        productOrderViewModel: ProductOrderViewModel
    ): ViewModel
}