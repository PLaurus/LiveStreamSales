package tv.wfc.livestreamsales.features.orderisconfirmed.di.modules.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.application.viewmodels.ViewModelProviderFactory
import tv.wfc.livestreamsales.features.orderisconfirmed.di.qualifiers.OrderIsConfirmedViewModelStoreOwner
import tv.wfc.livestreamsales.features.orderisconfirmed.di.scope.OrderIsConfirmedFeatureScope
import tv.wfc.livestreamsales.features.orderisconfirmed.viewmodel.IOrderIsConfirmedViewModel
import tv.wfc.livestreamsales.features.orderisconfirmed.viewmodel.OrderIsConfirmedViewModel

@Module
abstract class OrderIsConfirmedViewModelModule {
    companion object{
        @OrderIsConfirmedFeatureScope
        @Provides
        internal fun provideIOrderIsConfirmedViewModel(
            @OrderIsConfirmedViewModelStoreOwner
            viewModelStoreOwner: ViewModelStoreOwner,
            viewModelProviderFactory: ViewModelProviderFactory,
        ): IOrderIsConfirmedViewModel{
            return ViewModelProvider(
                viewModelStoreOwner,
                viewModelProviderFactory
            )[OrderIsConfirmedViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(OrderIsConfirmedViewModel::class)
    abstract fun bindOrderIsConfirmedViewModelIntoMap(
        orderIsConfirmedViewModel: OrderIsConfirmedViewModel
    ): ViewModel
}