package tv.wfc.livestreamsales.features.orderediting.di.modules.orderediting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.orderediting.di.qualifiers.OrderEditingViewModelStoreOwner
import tv.wfc.livestreamsales.features.orderediting.di.scope.OrderEditingFeatureScope
import tv.wfc.livestreamsales.features.orderediting.viewmodel.IOrderEditingViewModel
import tv.wfc.livestreamsales.features.orderediting.viewmodel.OrderEditingViewModel

@Module
abstract class OrderEditingViewModelModule {
    companion object{
        @OrderEditingFeatureScope
        @Provides
        internal fun provideIOrderEditingViewModel(
            @OrderEditingViewModelStoreOwner
            viewModelStoreOwner: ViewModelStoreOwner,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IOrderEditingViewModel {
            return ViewModelProvider(
                viewModelStoreOwner,
                viewModelProviderFactory
            )[OrderEditingViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(OrderEditingViewModel::class)
    internal abstract fun bindOrderEditingViewModelIntoMap(
        orderEditingViewModel: OrderEditingViewModel
    ): ViewModel
}